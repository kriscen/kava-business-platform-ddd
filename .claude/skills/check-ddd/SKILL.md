---
name: check-ddd
description: 检查业务模块是否符合项目 DDD 规范。当用户想审查模块结构、依赖方向、代码约定是否合规时使用。
license: MIT
compatibility: 无特殊依赖。
metadata:
  author: Kris
  version: '1.0'
  generatedBy: '1.0'
---

检查指定业务模块是否符合项目的 DDD 实现规范。生成结构化的合规报告。

**输入**：可选指定模块名称（如 `kbpd-upms`、`kbpd-member`）。如果省略，使用 **AskUserQuestion 工具** 让用户从可用的 DDD 业务模块中选择。

**步骤**

1. **确定目标模块**

   如果用户没有指定模块：
   - 扫描根 `pom.xml` 的 `<modules>` 部分，识别业务模块（排除 `kbpd-common`、`kbpd-auth`、`kbpd-gateway` 等非 DDD 模块）
   - 使用 **AskUserQuestion** 让用户选择要检查的模块

   确认模块路径存在：`{module}/pom.xml`

2. **检查 1：子模块结构完整性**

   读取 `{module}/pom.xml`，检查是否包含以下 7 个子模块：
   - `{module}-types`
   - `{module}-api`
   - `{module}-domain`
   - `{module}-application`
   - `{module}-infrastructure`
   - `{module}-adapter`
   - `{module}-bootstrap`

   对每个缺失的子模块记录 **CRITICAL**。

   同时检查每个子模块目录是否实际存在（而非仅在 pom 中声明）。

3. **检查 2：层间依赖方向（pom.xml）**

   读取各层 `pom.xml` 的 `<dependencies>` 部分，验证以下规则：

   **必须满足的依赖规则**：

   | 层 | 允许依赖 | 禁止依赖 |
   |---|---|---|
   | `types` | `kbpd-common-core` | 任何同模块其他层 |
   | `api` | `types` | `domain`, `application`, `infrastructure`, `adapter` |
   | `domain` | `types`, `spring-context` | `spring-boot-starter*`, `application`, `infrastructure`, `adapter`, MyBatis, 数据库驱动 |
   | `application` | `domain` | `infrastructure`, `adapter`, `api` |
   | `infrastructure` | `domain` | `adapter`, `application`（的接口可以，但不能依赖实现） |
   | `adapter` | `types`, `application`, `api` | `domain`（entity/aggregate/service/repository）, `infrastructure` |
   | `bootstrap` | `adapter`, `infrastructure` | `domain`, `application`（直接依赖） |

   对每个违规记录 **CRITICAL**，并给出具体的不应存在的依赖项。

   **特别检查**：
   - `domain` 层 pom 中不应出现 `mybatis`、`mybatis-plus`、`druid`、`mysql`、`spring-boot-starter-web` 等依赖
   - `adapter` 层 pom 中不应出现对 `{module}-domain` 的直接依赖（只允许通过 application 间接引用 domain.valobj）

4. **检查 3：Domain 层纯净性**

   在 `{module}-domain/src/main/java/` 下搜索以下违规模式：

   **禁止的 import**（使用 Grep 工具搜索）：
   - `@Autowired`、`@Resource` — 禁止字段注入
   - `org.springframework.web` — 禁止 Web 依赖
   - `org.apache.ibatis`、`com.baomidou` — 禁止持久化框架
   - `ApplicationContext`、`BeanFactory` — 禁止 Spring API 调用
   - 其他业务模块的 domain 包 — 禁止跨限界上下文依赖

   对每个违规记录 **CRITICAL**，给出文件路径和行号。

5. **检查 4：构造器注入规范**

   在 `{module}-domain/` 下找到所有 `@Service` 标注的类，检查：
   - 使用 `@Resource` 或 `@Autowired` 字段注入 → **CRITICAL**
   - 没有构造器但通过其他方式注入 → **WARNING**
   - 使用构造器注入（含 `final` 字段 + 构造器参数）→ 合规

   同样检查 `{module}-application/` 下的 `@Service` 类。

6. **检查 5：Repository 接口规范（CQRS）**

   在 `{module}-domain/src/main/java/**/repository/` 下检查：
   - 每个 Repository 接口是否继承自 `IBaseReadRepository` 或 `IBaseWriteRepository`（或两者的组合接口）
   - 是否存在既包含读又包含写的"混合"接口（不继承基类的）→ **WARNING**，建议拆分

   在 `{module}-infrastructure/` 下检查：
   - 每个 domain repository 接口是否都有对应的实现类
   - 实现类是否标注 `@Repository`

7. **检查 6：Adapter 引用边界**

   在 `{module}-adapter/src/main/java/` 下搜索以下违规 import：
   - `..domain.model.entity.*` — 禁止引用 Entity → **CRITICAL**
   - `..domain.model.aggregate.*` — 禁止引用 AggregateRoot → **CRITICAL**
   - `..domain.service.*` — 禁止引用 DomainService → **CRITICAL**
   - `..domain.repository.*` — 禁止引用 Repository → **CRITICAL**

   允许的 domain 引用：
   - `..domain.model.valobj.*` — ID 类型、ListQuery ✓

8. **检查 7：Converter 命名与分层**

   检查各层 Converter 是否符合命名约定：

   | 层 | 命名模式 | 转换方向 |
   |---|---|---|
   | adapter | `*AdapterConverter` | Request/Response ↔ AppDTO |
   | application | `*AppConverter` | Command/AppDTO ↔ DomainEntity |
   | infrastructure | `*Converter`（不带前缀） | DomainEntity ↔ PO |

   检查所有 Converter 都使用 `@Mapper(componentModel = "spring")`。
   命名不一致 → **SUGGESTION**。

9. **检查 8：ID 值对象规范**

   在 `{module}-domain/src/main/java/**/valobj/` 下检查 ID 类：
   - 是否使用 `@Value`（不可变）而非 `@Data`
   - 是否有 `static of()` 工厂方法
   - 是否实现 `Identifier` 接口

   在 `{module}-domain/src/main/java/**/valobj/` 下检查 ListQuery 类：
   - 是否实现 `ValueObject` 接口

10. **检查 9：Entity / AggregateRoot 规范**

    在 `{module}-domain/src/main/java/**/aggregate/` 下：
    - 检查类是否实现 `AggregateRoot<T>` 接口
    - 检查是否使用 `@Data` + `@Builder`

    在 `{module}-domain/src/main/java/**/entity/` 下：
    - 检查类是否实现 `Entity<T>` 接口

    不符合 → **WARNING**

11. **检查 10：Infrastructure 层结构**

    在 `{module}-infrastructure/src/main/java/` 下检查是否存在：
    - `dao/` 目录 — MyBatis-Plus Mapper 接口（应有 `@Mapper` 注解）
    - `dao/po/` 目录 — 持久化对象（应有 `@TableName` 注解）
    - `adapter/repository/` 目录 — Repository 实现
    - `converter/` 目录 — PO ↔ Entity 转换器

    缺失目录 → **WARNING**（可能是空的或尚未实现的子域）

12. **检查 11：Bootstrap 层纯净性**

    在 `{module}-bootstrap/` 下检查：
    - 是否有且仅有一个 `@SpringBootApplication` 主类
    - 是否不应包含业务逻辑代码（Controller、Service、Repository 等注解）→ **CRITICAL**
    - pom 是否依赖 `adapter` + `infrastructure`（完成依赖注入组装）

13. **生成报告**

    将所有发现汇总为结构化报告：

    ```
    ## DDD 合规报告：{module}

    ### 摘要

    | 检查项 | 状态 | 问题数 |
    |--------|------|--------|
    | 子模块结构 | ✅/❌ | N |
    | 层间依赖方向 | ✅/❌ | N |
    | Domain 层纯净性 | ✅/❌ | N |
    | 构造器注入 | ✅/❌ | N |
    | Repository CQRS | ✅/❌ | N |
    | Adapter 引用边界 | ✅/❌ | N |
    | Converter 命名 | ✅/❌ | N |
    | ID 值对象 | ✅/❌ | N |
    | Entity/AggregateRoot | ✅/❌ | N |
    | Infrastructure 结构 | ✅/❌ | N |
    | Bootstrap 纯净性 | ✅/❌ | N |

    ### CRITICAL（必须修复）

    - [检查项] 文件路径:行号 — 问题描述
      → 建议：...

    ### WARNING（建议修复）

    - [检查项] 文件路径:行号 — 问题描述
      → 建议：...

    ### SUGGESTION（可选改进）

    - [检查项] 描述
      → 建议：...

    ### 评估

    {整体评价和下一步建议}
    ```

    **严重程度规则**：
    - **CRITICAL**：违反核心架构约束（依赖方向、层间边界、Domain 纯净性）
    - **WARNING**：违反重要规范但不影响编译（命名约定、接口继承）
    - **SUGGESTION**：可改进但不紧急（风格一致性、缺失但可能不需要的文件）

**护栏规则**

- 只检查 DDD 业务模块（有 7 子模块结构的），跳过 `kbpd-common`、`kbpd-auth`、`kbpd-gateway` 等非 DDD 模块
- 如果子模块目录不存在但 pom 中声明了，同时报告结构缺失和依赖问题
- 空的子域（只有接口没有实现）不作为问题报告，但注明为"待实现"
- 检查基于实际代码内容，不猜测意图
- 如果模块尚未完整实现（如只有部分层有代码），只报告已有代码的问题，注明缺失的部分
