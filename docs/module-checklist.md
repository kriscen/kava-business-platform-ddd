# Kava DDD 业务模块生成检查清单

## 使用说明
此检查清单用于验证基于 kbpd-demo 模板生成的新业务模块是否完整且符合项目规范。

## 一、目录结构检查 ✅

### 1.1 主模块结构
```
kbpd-{module}/
├── [ ] pom.xml
├── [ ] kbpd-{module}-api/
├── [ ] kbpd-{module}-adapter/
├── [ ] kbpd-{module}-application/
├── [ ] kbpd-{module}-bootstrap/
├── [ ] kbpd-{module}-domain/
├── [ ] kbpd-{module}-infrastructure/
└── [ ] kbpd-{module}-types/
```

### 1.2 各子模块目录结构
#### API模块 (kbpd-{module}-api)
```
├── [ ] pom.xml
└── [ ] src/main/java/com/kava/kbpd/{module}/api/
    ├── [ ] model/
    │   ├── [ ] dto/{Entity}DTO.java
    │   ├── [ ] query/{Entity}Query.java
    │   ├── [ ] request/{Entity}Request.java
    │   └── [ ] response/
    │       ├── [ ] {Entity}Response.java
    │       └── [ ] {Entity}ListResponse.java
    └── [ ] service/IRemote{Entity}Service.java
```

#### Adapter模块 (kbpd-{module}-adapter)
```
├── [ ] pom.xml
└── [ ] src/main/java/com/kava/kbpd/{module}/trigger/
    ├── [ ] http/{Entity}Controller.java
    ├── [ ] converter/{Entity}TriggerConverter.java
    └── [ ] rpc/Remote{Entity}Service.java
```

#### Bootstrap模块 (kbpd-{module}-bootstrap)
```
├── [ ] pom.xml
└── [ ] src/main/
    ├── [ ] java/com/kava/kbpd/{module}/{Module}Application.java
    └── [ ] resources/
        ├── [ ] application.yml
        ├── [ ] application-dev.yml
        └── [ ] logback-spring.xml
```

#### Domain模块 (kbpd-{module}-domain)
```
├── [ ] pom.xml
└── [ ] src/main/java/com/kava/kbpd/{module}/domain/
    ├── [ ] model/
    │   ├── [ ] entity/{Entity}Entity.java
    │   └── [ ] valobj/
    │       ├── [ ] {Entity}Id.java
    │       └── [ ] {Entity}ListQuery.java
    ├── [ ] repository/I{Entity}Repository.java
    └── [ ] service/
        ├── [ ] I{Entity}Service.java
        └── [ ] impl/{Entity}Service.java
```

#### Infrastructure模块 (kbpd-{module}-infrastructure)
```
├── [ ] pom.xml
└── [ ] src/main/java/com/kava/kbpd/{module}/infrastructure/
    ├── [ ] adapter/repository/{Entity}Repository.java
    ├── [ ] converter/{Entity}Converter.java
    └── [ ] dao/
        ├── [ ] {Entity}Mapper.java
        └── [ ] po/{Entity}PO.java
```

#### Types模块 (kbpd-{module}-types)
```
├── [ ] pom.xml
└── [ ] src/main/java/com/kava/kbpd/{module}/types/
    ├── [ ] constants/{Module}Constants.java
    └── [ ] exception/
        ├── [ ] {Module}BizException.java
        └── [ ] {Module}BizErrorCodeEnum.java
```

## 二、代码规范检查 ✅

### 2.1 包名检查
- [ ] 所有包名遵循 `com.kava.kbpd.{module}` 规范
- [ ] 子包命名符合DDD分层要求
- [ ] 没有拼写错误

### 2.2 类名检查
- [ ] Entity类以 `{Entity}Entity` 结尾
- [ ] Service接口以 `I{Entity}Service` 命名
- [ ] Service实现类以 `{Entity}Service` 命名
- [ ] Repository接口以 `I{Entity}Repository` 命名
- [ ] Repository实现类以 `{Entity}Repository` 命名
- [ ] Controller类以 `{Entity}Controller` 结尾
- [ ] PO类以 `{Entity}PO` 结尾

### 2.3 注解检查
#### Domain层
- [ ] Entity类使用 `@Entity` 和 `@AggregateRoot` 注解
- [ ] Service实现类使用 `@Service` 注解
- [ ] ValueObject类使用 `@ValueObject` 注解

#### Infrastructure层
- [ ] Repository实现类使用 `@Repository` 注解
- [ ] Mapper接口使用 `@Mapper` 注解
- [ ] PO类使用 `@TableName` 注解

#### Adapter层
- [ ] Controller类使用 `@RestController` 注解
- [ ] 方法使用正确的HTTP方法注解

### 2.4 方法检查
#### Repository接口必须包含
- [ ] `{Entity}Id create({Entity}Entity entity)`
- [ ] `Boolean update({Entity}Entity entity)`
- [ ] `PagingInfo<{Entity}Entity> queryPage({Entity}ListQuery query)`
- [ ] `{Entity}Entity queryById({Entity}Id id)`
- [ ] `Boolean removeBatchByIds(List<{Entity}Id> ids)`

#### Service接口必须包含
- [ ] 与Repository接口相同的方法签名

#### Controller必须包含
- [ ] `GET /page` - 分页查询
- [ ] `GET /details` - 详情查询
- [ ] `POST` - 新增
- [ ] `PUT` - 更新
- [ ] `DELETE` - 删除

## 三、配置文件检查 ✅

### 3.1 Maven配置检查
#### 父pom.xml
- [ ] modules节点包含新模块
- [ ] dependencyManagement包含子模块依赖

#### 各子模块pom.xml
- [ ] parent配置正确
- [ ] artifactId命名正确
- [ ] 依赖关系正确

### 3.2 Bootstrap模块配置
#### application.yml
- [ ] `spring.application.name` 配置正确
- [ ] `server.port` 不与其他服务冲突
- [ ] `server.servlet.context-path` 配置正确
- [ ] Nacos配置正确

#### application-dev.yml
- [ ] 数据库配置正确
- [ ] 日志级别配置合理

## 四、依赖关系检查 ✅

### 4.1 模块间依赖
- [ ] bootstrap依赖adapter和infrastructure
- [ ] adapter依赖domain、api、types
- [ ] infrastructure依赖domain
- [ ] domain依赖types
- [ ] api依赖types

### 4.2 公共依赖
- [ ] 引入kbpd-common-core
- [ ] 引入kbpd-common-web（adapter模块）
- [ ] 引入kbpd-common-database（infrastructure模块）
- [ ] 引入spring-boot-starter-web
- [ ] 引入mybatis-plus-spring-boot3-starter

## 五、业务逻辑检查 ✅

### 5.1 数据转换
- [ ] TriggerConverter包含Request到Entity转换
- [ ] TriggerConverter包含Entity到Response转换
- [ ] InfrastructureConverter包含Entity到PO转换
- [ ] InfrastructureConverter包含PO到Entity转换

### 5.2 异常处理
- [ ] Controller方法返回统一的JsonResult格式
- [ ] Service层抛出业务异常
- [ ] 使用项目统一的异常处理机制

### 5.3 分页查询
- [ ] 使用PagingInfo进行分页响应
- [ ] Query对象继承BaseQuery
- [ ] 支持动态查询条件

## 六、编译和启动检查 ✅

### 6.1 编译检查
- [ ] `mvn clean compile` 无错误
- [ ] 所有import语句正确
- [ ] 没有语法错误

### 6.2 启动检查
- [ ] Application类能正常启动
- [ ] Nacos注册成功
- [ ] 数据库连接正常
- [ ] API接口可访问

## 七、测试检查 ✅

### 7.1 API测试
- [ ] GET /api/v1/{module}/{entity}/page 返回分页数据
- [ ] GET /api/v1/{module}/{entity}/details?id=1 返回详情
- [ ] POST /api/v1/{module}/{entity} 创建数据
- [ ] PUT /api/v1/{module}/{entity} 更新数据
- [ ] DELETE /api/v1/{module}/{entity} 删除数据

### 7.2 数据库测试
- [ ] 创建对应的数据库表
- [ ] MyBatis映射正常工作
- [ ] 数据CRUD操作正常

## 使用方法

1. **生成模块后**：使用此清单逐项检查
2. **发现问题**：记录具体问题并修复
3. **完成检查**：确保所有项目都已勾选
4. **最终验证**：运行编译和启动测试

## 常见问题和解决方案

### 问题1：编译错误
- **症状**：import语句报错
- **解决**：检查包名是否正确，依赖是否正确引入

### 问题2：启动失败
- **症状**：Spring Boot启动异常
- **解决**：检查配置文件、数据库连接、Bean注入

### 问题3：API访问404
- **症状**：接口调用返回404
- **解决**：检查Controller路径映射、context-path配置

### 问题4：数据库操作失败
- **症状**：SQL执行异常
- **解决**：检查Mapper配置、PO类注解、数据库表结构

---

**注意**：完成此检查清单后，新生成的业务模块应该能够正常运行并提供完整的CRUD功能。