# Kava DDD 业务模块生成AI提示词

## 快速生成提示词模板

```
我需要基于现有的 kbpd-demo 模板创建一个新的业务模块。

### 项目背景
- 项目采用DDD分层架构
- 使用Spring Boot 3.x + MyBatis Plus + Nacos
- 参考模块：kbpd-demo（模板）、kbpd-upms（实现示例）

### 需求信息
- **模块名称**: {替换为具体模块名，如order、goods、customer}
- **主实体名**: {替换为主要业务实体，如Order、Product、Customer}
- **业务场景**: {简要描述业务功能}
- **核心属性**: {列出主要业务字段}

### 生成要求
1. **严格遵循DDD分层架构**：api、adapter、application、bootstrap、domain、infrastructure、types
2. **参考kbpd-upms的实现模式**：特别是SysUser相关的所有类
3. **包含完整CRUD操作**：增删改查、分页查询
4. **确保命名规范一致**：包名、类名、字段名遵循项目约定
5. **生成所有必要文件**：pom.xml、配置文件、Java类文件
6. **保持依赖关系正确**：模块间依赖、公共模块集成

### 具体生成内容
- 完整的7个子模块目录结构
- 主实体的DDD建模（Entity、ValueObject、Repository、Service）
- REST API控制器和所有转换器
- MyBatis映射器和PO对象
- 完整的Maven配置
- 应用启动类和配置文件

### 实现参考
- Domain层参考：kbpd-upms的SysUserEntity、SysUserService
- Infrastructure层参考：SysUserRepository、SysUserMapper、SysUserPO
- Adapter层参考：SysUserController、SysUserTriggerConverter
- API层参考：SysUserRequest、SysUserResponse、SysUserQuery

请严格按照这个模式生成完整的模块代码。
```

## 具体模块示例

### 订单模块生成提示词
```
我需要基于现有的 kbpd-demo 模板创建一个新的业务模块。

### 项目背景
- 项目采用DDD分层架构
- 使用Spring Boot 3.x + MyBatis Plus + Nacos
- 参考模块：kbpd-demo（模板）、kbpd-upms（实现示例）

### 需求信息
- **模块名称**: order
- **主实体名**: Order
- **业务场景**: 电商订单管理系统，负责订单的完整生命周期管理
- **核心属性**: 
  - id: Long (主键)
  - orderNo: String (订单号)
  - customerId: Long (客户ID)
  - totalAmount: BigDecimal (订单总金额)
  - status: Integer (订单状态)
  - remark: String (备注)
  - createTime: LocalDateTime (创建时间)
  - updateTime: LocalDateTime (更新时间)

### 生成要求
1. **严格遵循DDD分层架构**：api、adapter、application、bootstrap、domain、infrastructure、types
2. **参考kbpd-upms的实现模式**：特别是SysUser相关的所有类
3. **包含完整CRUD操作**：增删改查、分页查询
4. **确保命名规范一致**：包名、类名、字段名遵循项目约定
5. **生成所有必要文件**：pom.xml、配置文件、Java类文件
6. **保持依赖关系正确**：模块间依赖、公共模块集成

### 具体生成内容
- 完整的7个子模块目录结构
- 主实体的DDD建模（Entity、ValueObject、Repository、Service）
- REST API控制器和所有转换器
- MyBatis映射器和PO对象
- 完整的Maven配置
- 应用启动类和配置文件

### 实现参考
- Domain层参考：kbpd-upms的SysUserEntity、SysUserService
- Infrastructure层参考：SysUserRepository、SysUserMapper、SysUserPO
- Adapter层参考：SysUserController、SysUserTriggerConverter
- API层参考：SysUserRequest、SysUserResponse、SysUserQuery

请严格按照这个模式生成完整的模块代码。
```

### 商品模块生成提示词
```
我需要基于现有的 kbpd-demo 模板创建一个新的业务模块。

### 项目背景
- 项目采用DDD分层架构
- 使用Spring Boot 3.x + MyBatis Plus + Nacos
- 参考模块：kbpd-demo（模板）、kbpd-upms（实现示例）

### 需求信息
- **模块名称**: goods
- **主实体名**: Product
- **业务场景**: 商品信息管理系统，负责商品基础信息维护
- **核心属性**: 
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

### 生成要求
1. **严格遵循DDD分层架构**：api、adapter、application、bootstrap、domain、infrastructure、types
2. **参考kbpd-upms的实现模式**：特别是SysUser相关的所有类
3. **包含完整CRUD操作**：增删改查、分页查询
4. **确保命名规范一致**：包名、类名、字段名遵循项目约定
5. **生成所有必要文件**：pom.xml、配置文件、Java类文件
6. **保持依赖关系正确**：模块间依赖、公共模块集成

### 具体生成内容
- 完整的7个子模块目录结构
- 主实体的DDD建模（Entity、ValueObject、Repository、Service）
- REST API控制器和所有转换器
- MyBatis映射器和PO对象
- 完整的Maven配置
- 应用启动类和配置文件

### 实现参考
- Domain层参考：kbpd-upms的SysUserEntity、SysUserService
- Infrastructure层参考：SysUserRepository、SysUserMapper、SysUserPO
- Adapter层参考：SysUserController、SysUserTriggerConverter
- API层参考：SysUserRequest、SysUserResponse、SysUserQuery

请严格按照这个模式生成完整的模块代码。
```

## 质量检查提示词

生成模块后，使用以下提示词进行质量检查：

```
请检查刚才生成的 kbpd-{module} 模块是否符合以下标准：

### 结构检查
1. 是否包含7个完整的子模块？
2. 每个子模块是否都有正确的pom.xml配置？
3. 包名是否遵循com.kava.kbpd.{module}的规范？

### 代码检查
1. Entity类是否正确使用了@Entity和@AggregateRoot注解？
2. Repository接口是否包含完整的CRUD方法？
3. Controller是否包含完整的REST API端点？
4. Converter类是否正确处理了对象转换？

### 配置检查
1. application.yml中的服务名和端口是否正确？
2. 父pom.xml是否添加了新模块的module声明？
3. bootstrap模块是否正确依赖了adapter和infrastructure？

### 依赖检查
1. 是否正确引入了kbpd-common系列依赖？
2. MyBatis Plus配置是否正确？
3. 是否缺少必要的Spring依赖？

如发现问题，请指出具体位置并提供修复建议。
```

## 使用说明

1. **选择合适的模板**：根据业务需求选择对应的提示词模板
2. **填写具体信息**：将模板中的占位符替换为实际的业务信息
3. **执行生成**：将完整的提示词提交给AI助手
4. **质量检查**：使用检查提示词验证生成结果
5. **调整完善**：根据检查结果进行必要的调整

通过这套提示词模板，可以确保生成的业务模块与项目架构高度一致，减少手工调整的工作量。