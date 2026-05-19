## Context

Area（行政区划）是 UPMS 中的通用基础数据子域，数据由 SQL 脚本初始化导入，运行时主要为只读消费。当前模块存在多处功能缺陷（ListDTO 为空、查询过滤失效），且只提供全量树查询，缺少按层级懒加载和按类型筛选的能力。

现有代码遵循完整的 DDD 分层（domain → application → adapter/infrastructure），使用 MyBatis-Plus 做持久化，Hutool TreeUtil 做树构建。

## Goals / Non-Goals

**Goals:**
- 修复现有 bug（ListDTO 为空、查询过滤无效、多余字段）
- 补充常用的查询接口（按父节点查子节点、树按类型筛选）
- 让枚举类型在 domain 层实际落地使用
- 保持 Area 作为轻量只读基础模块的定位

**Non-Goals:**
- 不实现按 adcode 精确查询（等高德接入再做）
- 不实现级联删除（基础数据不需要）
- 不引入缓存层（数据量约 3000 条，查询频率可控）
- 不做 RPC 接口暴露（当前无跨服务消费需求）

## Decisions

### D1: areaType 使用 SysAreaType 枚举替代 String

**Because:** SysAreaType 枚举已存在于 types 模块，代表有业务语义的层级分类（国家/省/市/区县）。Entity 层用 String 丢失了类型安全，编译期无法拦截非法值。改用枚举后，转换逻辑集中在 Infrastructure Converter（PO ↔ Entity），与现有项目的 MapStruct 模式一致。

### D2: areaStatus 复用 kbpd-common 的 Status 通用枚举

**Because:** "0未生效/1生效" 是平台通用的二元状态模式，`Status` 枚举已存在于 `kbpd-common-core`。没必要为每个实体单独定义状态枚举。Entity 层使用 `Status` 枚举，PO 层保持 String，Converter 层做转换。

### D3: 新增 /children 接口返回扁平列表而非子树

**Because:** 懒加载场景下前端每次只展开一个节点，需要的是该节点的直接子节点列表，不需要递归构建子树。返回扁平列表更简洁，前端自行渲染。

### D4: tree 接口通过 areaType 参数控制层级

**Because:** 实际场景中可能只需要"省级列表"或"省→市"两级树。通过 areaType 过滤可以减少数据量和构建开销，而不需要改树构建逻辑本身——在 SQL 查询阶段加 WHERE 条件即可。

### D5: 写入接口保留但不做重点增强

**Because:** 地区数据主要由 SQL 初始化导入，但保留写入接口方便后续管理后台手动维护。仅做基本的非空校验，不做复杂的级联或唯一性校验。

## Risks / Trade-offs

- **[枚举转换遗漏]** → Infrastructure Converter 中 String ↔ Enum 转换需确保 null 安全，MapStruct 默认处理得当但需验证
- **[tree + areaType 过滤可能产生断层]** → 例如过滤 areaType=1（省级），但省的 pid 指向国家节点，国家节点不在结果中，树根会丢失。需要在构建树时使用 0 或 areaType 对应的父级 pid 作为根节点
