# Kava Business Platform DDD 业务模块生成指南

## 概述

本指南提供了基于 `kbpd-demo` 模板创建新业务模块的完整提示词和规则，帮助快速生成符合项目架构规范的业务模块（如 order、goods 等）。

## 一、架构分析

### 1.1 DDD分层架构
```
kbpd-{module}/
├── kbpd-{module}-api/           # API层：对外接口定义
├── kbpd-{module}-adapter/       # 适配器层：HTTP控制器、RPC服务
├── kbpd-{module}-application/   # 应用层：业务流程编排
├── kbpd-{module}-bootstrap/     # 启动层：应用启动入口
├── kbpd-{module}-domain/        # 领域层：核心业务逻辑
├── kbpd-{module}-infrastructure/# 基础设施层：数据持久化
├── kbpd-{module}-types/         # 类型层：枚举、常量、异常
└── pom.xml                      # 模块聚合配置
```

### 1.2 关键设计模式
- **Repository模式**: 数据访问抽象
- **Entity-Value Object**: 领域对象建模
- **Converter模式**: 层间数据转换
- **Service层**: 业务逻辑封装

## 二、业务模块生成提示词模板

### 2.1 主提示词

```
请基于 kbpd-demo 模板为我创建一个新的业务模块：kbpd-{模块名}

## 模块需求
- 模块名称: {模块名} (如：order、goods、customer等)
- 主要实体: {主实体名称} (如：Order、Product、Customer等)
- 业务描述: {简要描述业务场景}

## 生成要求
1. 严格遵循项目现有的DDD分层架构
2. 使用与 kbpd-upms 相同的代码结构和命名规范
3. 包含完整的CRUD操作和分页查询
4. 生成所有必要的配置文件和依赖关系
5. 确保与现有公共模块的集成

## 具体生成内容
- 完整的7个子模块（api、adapter、application、bootstrap、domain、infrastructure、types）
- 主实体的完整DDD建模（Entity、ValueObject、Repository等）
- REST API控制器和转换器
- 数据库映射和配置
- Maven依赖配置
```

### 2.2 具体模块生成提示词

```
现在开始生成 kbpd-{模块名} 模块，请按以下步骤执行：

### 第一步：创建模块结构
1. 复制 kbpd-demo 的完整目录结构
2. 将所有文件中的 "demo" 替换为 "{模块名}"
3. 调整父pom.xml，添加新模块的module声明

### 第二步：生成核心业务实体
基于 kbpd-upms 中的 SysUser 实体模式，创建：
- {实体名}Entity（domain层）
- {实体名}Id（ValueObject）
- {实体名}ListQuery（查询对象）
- I{实体名}Repository（仓储接口）
- I{实体名}Service（领域服务接口）

### 第三步：实现基础设施层
参照 SysUserRepository 模式，创建：
- {实体名}Repository（仓储实现）
- {实体名}Mapper（MyBatis映射器）
- {实体名}PO（持久化对象）
- {实体名}Converter（转换器）

### 第四步：实现适配器层
参照 SysUserController 模式，创建：
- {实体名}Controller（REST控制器）
- {实体名}TriggerConverter（适配器转换器）

### 第五步：完善API层
创建完整的API模型：
- {实体名}Request/Response/Query（请求响应模型）
- IRemote{实体名}Service（远程服务接口）

### 第六步：配置和启动
- 更新bootstrap模块的配置文件
- 配置数据库连接和MyBatis映射
- 确保应用能正常启动
```

## 三、生成规则和检查清单

### 3.1 命名规范
- **模块名**: kbpd-{business} (全小写，短横线分隔)
- **包名**: com.kava.kbpd.{business}
- **类名**: {Business}Entity, {Business}Service (首字母大写)
- **字段名**: 驼峰命名法
- **常量**: 全大写下划线分隔

### 3.2 必须生成的文件清单

#### 3.2.1 根目录文件
- [ ] pom.xml (模块聚合配置)

#### 3.2.2 API模块 (kbpd-{module}-api)
- [ ] pom.xml
- [ ] src/main/java/com/kava/kbpd/{module}/api/service/IRemote{Entity}Service.java
- [ ] src/main/java/com/kava/kbpd/{module}/api/model/dto/{Entity}DTO.java
- [ ] src/main/java/com/kava/kbpd/{module}/api/model/query/{Entity}Query.java
- [ ] src/main/java/com/kava/kbpd/{module}/api/model/request/{Entity}Request.java
- [ ] src/main/java/com/kava/kbpd/{module}/api/model/response/{Entity}Response.java
- [ ] src/main/java/com/kava/kbpd/{module}/api/model/response/{Entity}ListResponse.java

#### 3.2.3 Adapter模块 (kbpd-{module}-adapter)
- [ ] pom.xml
- [ ] src/main/java/com/kava/kbpd/{module}/trigger/http/{Entity}Controller.java
- [ ] src/main/java/com/kava/kbpd/{module}/trigger/converter/{Entity}TriggerConverter.java
- [ ] src/main/java/com/kava/kbpd/{module}/trigger/rpc/Remote{Entity}Service.java

#### 3.2.4 Application模块 (kbpd-{module}-application)
- [ ] pom.xml

#### 3.2.5 Bootstrap模块 (kbpd-{module}-bootstrap)
- [ ] pom.xml
- [ ] src/main/java/com/kava/kbpd/{module}/{Module}Application.java
- [ ] src/main/resources/application.yml
- [ ] src/main/resources/application-dev.yml
- [ ] src/main/resources/logback-spring.xml

#### 3.2.6 Domain模块 (kbpd-{module}-domain)
- [ ] pom.xml
- [ ] src/main/java/com/kava/kbpd/{module}/domain/model/entity/{Entity}Entity.java
- [ ] src/main/java/com/kava/kbpd/{module}/domain/model/valobj/{Entity}Id.java
- [ ] src/main/java/com/kava/kbpd/{module}/domain/model/valobj/{Entity}ListQuery.java
- [ ] src/main/java/com/kava/kbpd/{module}/domain/repository/I{Entity}Repository.java
- [ ] src/main/java/com/kava/kbpd/{module}/domain/service/I{Entity}Service.java
- [ ] src/main/java/com/kava/kbpd/{module}/domain/service/impl/{Entity}Service.java

#### 3.2.7 Infrastructure模块 (kbpd-{module}-infrastructure)
- [ ] pom.xml
- [ ] src/main/java/com/kava/kbpd/{module}/infrastructure/adapter/repository/{Entity}Repository.java
- [ ] src/main/java/com/kava/kbpd/{module}/infrastructure/converter/{Entity}Converter.java
- [ ] src/main/java/com/kava/kbpd/{module}/infrastructure/dao/{Entity}Mapper.java
- [ ] src/main/java/com/kava/kbpd/{module}/infrastructure/dao/po/{Entity}PO.java

#### 3.2.8 Types模块 (kbpd-{module}-types)
- [ ] pom.xml
- [ ] src/main/java/com/kava/kbpd/{module}/types/constants/{Module}Constants.java
- [ ] src/main/java/com/kava/kbpd/{module}/types/exception/{Module}BizException.java
- [ ] src/main/java/com/kava/kbpd/{module}/types/exception/{Module}BizErrorCodeEnum.java

### 3.3 依赖关系检查
- [ ] 父pom.xml已添加新模块的module声明
- [ ] 各子模块的parent正确指向父模块
- [ ] bootstrap模块包含adapter和infrastructure依赖
- [ ] 公共依赖已正确引入（kbpd-common系列）

### 3.4 配置文件检查
- [ ] application.yml中服务名称、端口配置正确
- [ ] application-dev.yml中数据库配置正确
- [ ] Nacos配置正确
- [ ] 日志配置正确

## 四、实际使用示例

### 4.1 创建订单模块示例
```
请基于 kbpd-demo 模板为我创建一个新的业务模块：kbpd-order

## 模块需求
- 模块名称: order
- 主要实体: Order (订单)
- 业务描述: 电商订单管理，包含订单创建、查询、更新、删除等功能

## 订单实体属性
- id: Long (主键)
- orderNo: String (订单号)
- customerId: Long (客户ID)
- totalAmount: BigDecimal (订单总金额)
- status: Integer (订单状态：1-待支付，2-已支付，3-已发货，4-已完成，5-已取消)
- createTime: LocalDateTime (创建时间)
- updateTime: LocalDateTime (更新时间)

请严格按照前面的生成规则和检查清单执行。
```

### 4.2 创建商品模块示例
```
请基于 kbpd-demo 模板为我创建一个新的业务模块：kbpd-goods

## 模块需求
- 模块名称: goods
- 主要实体: Product (商品)
- 业务描述: 商品信息管理，包含商品的基本信息维护

## 商品实体属性
- id: Long (主键)
- productCode: String (商品编码)
- productName: String (商品名称)
- categoryId: Long (分类ID)
- price: BigDecimal (价格)
- stock: Integer (库存数量)
- status: Integer (状态：1-上架，2-下架)
- description: String (商品描述)
- createTime: LocalDateTime (创建时间)
- updateTime: LocalDateTime (更新时间)

请严格按照前面的生成规则和检查清单执行。
```

## 五、注意事项

### 5.1 技术约束
- 必须使用Java 21
- 严格遵循Spring Boot 3.x规范
- 使用MyBatis Plus进行数据操作
- 遵循DDD分层架构原则

### 5.2 代码质量要求
- 使用Lombok减少样板代码
- 添加必要的注释和文档
- 遵循项目现有的代码风格
- 确保异常处理完整

### 5.3 集成要求
- 与kbpd-common模块正确集成
- 支持Nacos服务注册发现
- 支持统一的JSON响应格式
- 支持统一的异常处理机制

---

## 附录：快速生成命令模板

```bash
# 1. 生成模块目录结构
mkdir -p kbpd-{module}/{kbpd-{module}-api,kbpd-{module}-adapter,kbpd-{module}-application,kbpd-{module}-bootstrap,kbpd-{module}-domain,kbpd-{module}-infrastructure,kbpd-{module}-types}

# 2. 复制基础pom.xml文件
cp kbpd-demo/pom.xml kbpd-{module}/pom.xml
cp kbpd-demo/kbpd-demo-*/pom.xml kbpd-{module}/kbpd-{module}-*/

# 3. 批量替换文件内容
find kbpd-{module} -name "*.xml" -exec sed -i 's/demo/{module}/g' {} \;
find kbpd-{module} -name "*.yml" -exec sed -i 's/demo/{module}/g' {} \;
```

使用此指南，可以快速、标准化地生成符合项目架构的新业务模块。