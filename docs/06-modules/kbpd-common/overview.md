# kbpd-common — 公共基础组件库

## 模块定位

`kbpd-common` 是平台的公共基础组件集合，为所有业务模块提供统一的基础设施能力。它本身不实现业务逻辑，而是以 Spring Boot Starter 和工具库的形式被各业务模块引用。

**核心职责**：

- 统一管理全平台第三方依赖版本（BOM）
- 提供 DDD 基础类型体系（值对象、实体、聚合根标识接口）
- 封装 Redis / MyBatis-Plus / Spring Security / Web 层的通用配置
- 定义全局异常体系、错误码、常量和枚举

**不属于本模块的职责**：

- 具体业务逻辑 → 各业务模块（`kbpd-upms`、`kbpd-member` 等）
- 服务发现与路由 → `kbpd-gateway`
- 认证协议处理 → `kbpd-auth`

## 子模块概览

| 子模块 | 打包方式 | 核心能力 | 自动配置 |
|--------|---------|----------|---------|
| `kbpd-common-bom` | `pom` (BOM) | 集中管理所有第三方及内部依赖版本 | — |
| `kbpd-common-core` | `jar` | DDD 类型体系、基类、异常框架、常量枚举 | — |
| `kbpd-common-cache` | `jar` (Starter) | Redis/Redisson 配置与封装 | 自动装配 |
| `kbpd-common-database` | `jar` (Starter) | MyBatis-Plus 配置、PO 基类体系 | 自动装配 |
| `kbpd-common-security` | `jar` (Starter) | OAuth2 Resource Server 配置 | `@EnableResourceServer` 激活 |
| `kbpd-common-web` | `jar` (Starter) | Web 层通用配置（Jackson Long→String） | 自动装配 |

## 子模块依赖关系

```
kbpd-common-bom          (独立 BOM，无 parent 链接到 kbpd-common)
   │
   │  版本管理
   ▼
kbpd-common-core         (依赖: hutool-core)
   ▲
   │
kbpd-common-cache        (依赖: core, redisson, spring-data-redis)
   ▲
   │
kbpd-common-security     (依赖: cache, core, spring-security, oauth2-resource-server)

kbpd-common-database     (依赖: mybatis-plus-spring-boot3-starter)
kbpd-common-web          (依赖: spring-boot-starter-web, undertow)
```

---

## kbpd-common-bom

集中管理所有内部模块和第三方依赖的版本号，作为整个项目的版本锚点。

**关键版本管理**：

| 依赖 | 版本 | 用途 |
|------|------|------|
| MyBatis-Plus | 3.5.9 | ORM 框架 |
| Apache Dubbo | 3.3.4 | RPC 通信 |
| Redisson | 3.23.4 | Redis 客户端 |
| Druid | 1.2.23 | 数据库连接池 |
| Hutool | 5.8.36 | 通用工具库 |
| MapStruct | 1.6.3 | 对象映射 |
| Knife4j + SpringDoc | 3.0.5 / 2.1.0 | API 文档 |

> **注意**：`kbpd-common-bom` 不在 `kbpd-common/pom.xml` 的 `<modules>` 中，它是独立发布的 BOM artifact，各业务模块通过 `<dependencyManagement>` 引入。

## kbpd-common-core

平台最底层的工具库，不依赖 Spring 框架，被所有其他 common 子模块引用。

### DDD 标识接口体系

定义 DDD 分层架构的核心类型标记：

```
ValueObject (值对象标记, extends Serializable)
  └── Identifier (唯一标识标记)
Entity<T extends Identifier> (实体, has identifier() 方法)
  └── AggregateRoot<T extends Identifier> (聚合根标记)
```

### 基类

| 类名 | 用途 |
|------|------|
| `JsonResult<T>` | 统一 API 响应包装（`success`/`data`/`errorCode`/`errorMessage`） |
| `PagingInfo<T>` | 分页查询结果（`pageNo`/`pageSize`/`total`/`list`） |
| `BaseEvent<T>` | 领域事件基类（含 `EventMessage<T>` 内部类） |
| `AdapterBaseListQuery` | Adapter 层分页查询参数基类 |
| `QueryParamValObj` | 分页查询参数值对象 |

### 仓储接口（CQRS 模式）

| 接口 | 职责 |
|------|------|
| `IBaseReadRepository<I, E>` | 读侧：`queryById` |
| `IBaseWriteRepository<I, E>` | 写侧：`create`、`update`、`removeBatchByIds` |
| `IBaseSimpleRepository<I, E, Q>` | 读写合一：包含完整 CRUD + 分页查询 |

### 异常框架

```
BaseErrorCodeEnum (接口: getErrorCode(), getErrorMsg())
  └── CommonErrorCodeEnum (通用错误码枚举)
      -1 未知错误
      1001~1006 客户端错误（HTTP 方法 / Body 校验 / JSON 格式 / 路径变量 / 参数校验）
      2001 服务端参数非法

BaseBizException (运行时异常, 支持 MessageFormat 参数替换)
```

### 值对象 ID

| 类 | 包装类型 |
|----|---------|
| `SysUserId` | `Long id` |
| `SysTenantId` | `Long id` |

### 常量与枚举

**常量**：`CoreConstant`（时区、日期格式）、`CollectionSize`、`PathType`、`JwtClaimConstants`（JWT Claim 键名）、`SecretConstants`

**枚举**：`YesNoEnum`、`Status`（启用/禁用）、`UserType`（B 端/C 端用户）、`ScopeType`（认证范围分类）

## kbpd-common-cache

基于 Redisson + Spring Data Redis 的缓存基础设施封装。

### 配置项

配置前缀：`kbpd.redis`

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `host` | String | — | Redis 地址 |
| `port` | Integer | — | Redis 端口 |
| `password` | String | — | Redis 密码 |
| `pool-size` | Integer | 64 | 连接池大小 |
| `min-idle-size` | Integer | 10 | 最小空闲连接数 |
| `idle-timeout` | Long | 10000 | 空闲超时（ms） |
| `connect-timeout` | Long | 10000 | 连接超时（ms） |
| `retry-attempts` | Integer | 3 | 命令重试次数 |
| `retry-interval` | Long | 1000 | 重试间隔（ms） |
| `keep-alive` | Boolean | true | TCP Keep-Alive |

### 核心组件

| 组件 | 说明 |
|------|------|
| `IRedisService` / `RedissonService` | Redis 统一服务接口及实现，涵盖 KV、队列、原子操作、集合、锁、信号量、布隆过滤器等 |
| `RedisKeyGenerator` | Key 生成器，格式：`{env}:{module}:{keyType}[:{id}]` |
| `RedisKeyModule` | Key 模块前缀枚举：`AUTH`、`UPMS` |
| `CustomJsonJacksonCodec` | Redisson 自定义 JSON 编解码器，复用应用全局 Jackson ObjectMapper |

## kbpd-common-database

基于 MyBatis-Plus 的数据库基础设施封装。

### PO 基类体系

为不同业务场景提供分层的持久化对象基类，支持审计字段自动填充和软删除：

```
BasePO (Serializable 空基类)
  ├── SysCreatedPO (id, creator, gmtCreate)
  │   └── SysUpdatedPO (+ modifier, gmtModified)
  │       └── SysDeletablePO (+ delFlag @TableLogic)
  └── TenantCreatedPO (id, creator, gmtCreate, tenantId)
      └── TenantUpdatedPO (+ modifier, gmtModified)
          └── TenantDeletablePO (+ delFlag @TableLogic)
```

- 所有审计字段通过 MyBatis-Plus `@TableField(fill = ...)` 自动填充
- `delFlag` 使用 `@TableLogic` 实现软删除
- `Tenant*` 系列基类额外包含 `tenantId` 字段，支持多租户场景

### 配置

自动装配 `MybatisPlusConfig`，包含：

- `PaginationInnerInterceptor`（MySQL 分页插件）
- `StringArrayTypeHandler`（Java `String[]` ↔ 数据库逗号分隔字符串）

### 辅助类

| 类 | 说明 |
|----|------|
| `StringArrayTypeHandler` | MyBatis 类型处理器：`String[]` ↔ 逗号分隔字符串 |
| `DataScopeType` | 数据权限级别枚举：`ALL`、`CUSTOM`、`OWN_CHILD_LEVEL`、`OWN_LEVEL`、`SELF_LEVEL` |

## kbpd-common-security

基于 Spring Security + OAuth2 Resource Server 的安全基础设施。

### 激活方式

不使用自动装配，而是通过注解按需激活：

```java
@EnableResourceServer  // 在任意 @Configuration 类上添加此注解
```

> **注意**：安全配置通过 `@Profile("!dev")` 控制，开发环境（`dev` profile）下不生效。

### 配置项

配置前缀：`kbpd.security`

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `whitelist-paths` | List\<String\> | — | 白名单路径，无需认证 |

### 核心行为

- **Security Filter Chain**：白名单路径放行，其余请求需 JWT 认证，禁用 CSRF
- **JWT 解析**：从 `authorities` Claim 提取权限，无前缀
- **Swagger 放行**：自动忽略 `/webjars/**`、`/doc.html`、`/swagger-resources/**`、`/v3/api-docs/**` 等路径
- **密码编码器**：`DelegatingPasswordEncoder`（支持多种编码格式，推荐 BCrypt）

### 工具类

`SecurityUtils` 提供静态方法从 JWT 中提取当前用户信息：

| 方法 | 返回值来源 |
|------|-----------|
| `getUserId()` | JWT `userId` Claim |
| `getUsername()` | `Authentication.getName()` |
| `getDeptId()` | JWT `deptId` Claim |
| `getMemberId()` | JWT `memberId` Claim |
| `getRoles()` | Authorities 集合 |
| `getTokenAttributes()` | 完整 JWT Claims Map |

## kbpd-common-web

Web 层通用配置，主要解决 JavaScript 长整型精度丢失问题。

### 核心配置

`WebConfig` 自定义 Jackson `ObjectMapper`：

- **Long → String**：将 `Long` 和 `BigInteger` 序列化为字符串，防止前端 JS 精度丢失（JS `Number.MAX_SAFE_INTEGER` = 2^53）
- 使用 Undertow 替代默认 Tomcat 作为嵌入式 Servlet 容器
