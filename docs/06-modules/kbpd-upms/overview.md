# kbpd-upms -- 用户权限管理服务

## 模块定位

kbpd-upms 是平台的**用户权限管理**核心服务，负责用户、角色、权限、租户等基础数据的全生命周期管理。

**核心职责：**

- 用户管理（User）：账号创建、密码策略、锁定/解锁
- 角色管理（Role）：角色定义、数据权限范围
- 菜单权限（Menu）：树形菜单、按钮级权限、菜单作用域
- 部门管理（Dept）：组织架构树
- 租户管理（Tenant）：多租户隔离、租户域名绑定
- 地区数据（Area）：省市区树形数据
- 日志审计（Log / AuditLog）：操作日志、字段变更审计
- 文件管理（File / FileGroup）：文件及文件分组
- 国际化（I18n）：多语言键值对
- 公共参数（PublicParam）：系统级参数配置
- 网关路由（RouteConf）：动态路由配置
- OAuth 客户端（OauthClient）：OAuth2 客户端信息管理

**不负责：**

- Token 签发与验证 → 由 kbpd-auth 负责
- 请求路由与限流 → 由 kbpd-gateway 负责
- 会员业务逻辑 → 由 kbpd-member 负责

---

## 技术选型

| 技术 | 版本 | 用途 |
|---|---|---|
| Java | 21 | 运行时 |
| Spring Boot | 3.3.x | 应用框架 |
| Spring Cloud | 2023.x | 微服务基础设施 |
| Nacos | — | 服务发现与配置中心 |
| Apache Dubbo | 3.3.x | RPC 通信（向 auth 等服务暴露用户/客户端信息） |
| MyBatis-Plus | 3.5.x | ORM 与分页 |
| MySQL | — | 持久化存储 |
| Redis | — | 缓存（通过 kbpd-common-cache） |
| MapStruct | — | 层间对象映射（Entity ↔ PO、Command ↔ Request 等） |
| Lombok | — | 减少样板代码 |
| Hutool | — | 通用工具（TreeUtil 构建地区树等） |

---

## 目录结构

```
kbpd-upms/
├── kbpd-upms-api/                    # 对外契约：Request/Response/Query/DTO/远程接口定义
│   ├── model/request/                #   14 个 Request 类（对应 14 个资源）
│   ├── model/response/               #   ListResponse + DetailResponse（共 28 个）
│   ├── model/query/                  #   Adapter 层分页查询参数
│   ├── model/dto/                    #   跨服务传输 DTO（SysUserDTO、SysOauthClientDTO）
│   └── service/                      #   Dubbo 远程接口（IRemoteUserService、IRemoteOauthClientService）
├── kbpd-upms-types/                  # 共享类型：枚举、常量、异常定义
│   ├── enums/                        #   SysMenuType、SysAreaType、SysRoleDataScope 等
│   ├── constants/                    #   UpmsConstants
│   └── exception/                    #   UpmsBizException、UpmsBizErrorCodeEnum
├── kbpd-upms-domain/                 # 领域层：实体、聚合、领域服务、仓储接口
│   ├── model/aggregate/              #   SysUserEntity、SysRoleEntity（聚合根）
│   ├── model/entity/                 #   Menu、Dept、Tenant、Area 等 12 个实体
│   ├── model/valobj/                 #   值对象 ID（12 个）+ 列表查询对象（14 个）
│   ├── service/                      #   领域服务接口
│   ├── service/impl/                 #   领域服务实现
│   └── repository/                   #   仓储接口（Read/Write 分离 + Simple）
├── kbpd-upms-application/            # 应用层：应用服务、命令、DTO、转换器
│   ├── service/                      #   14 个应用服务接口
│   ├── service/impl/                 #   应用服务实现
│   ├── model/command/                #   Create/Update 命令（共 28 个）
│   ├── model/dto/                    #   App 层 List/Detail DTO（共 28 个）
│   └── converter/                    #   MapStruct 转换器（14 个）
├── kbpd-upms-infrastructure/         # 基础设施层：仓储实现、Mapper、PO、转换器
│   ├── adapter/repository/           #   仓储实现（16 个）
│   ├── dao/                          #   MyBatis-Plus Mapper 接口（14 个）
│   ├── dao/po/                       #   持久化对象（16 个，含关联表 PO）
│   └── converter/                    #   Entity ↔ PO 转换器（14 个）
├── kbpd-upms-adapter/                # 适配器层：Controller、RPC 服务、转换器
│   ├── http/                         #   REST Controller（14 个）
│   ├── rpc/                          #   Dubbo 服务实现（2 个）
│   └── converter/                    #   Request/Response ↔ Command/DTO 转换器（14 个）
└── kbpd-upms-bootstrap/              # 启动模块
    └── src/main/java/                #   UpmsApplication + DevSecurityConfig
```

---

## 架构设计

### DDD 分层与依赖方向

```
┌─────────────────────────────────────────────────┐
│                  adapter (HTTP/RPC)              │
│    Controller / RemoteService / AdapterConverter │
├─────────────────────────────────────────────────┤
│                  application                     │
│    AppService / Command / AppDTO / AppConverter  │
├─────────────────────────────────────────────────┤
│                    domain                        │
│   Aggregate / Entity / ValObj / DomainService    │
│                Repository Interface              │
├─────────────────────────────────────────────────┤
│                 infrastructure                   │
│     RepositoryImpl / Mapper / PO / Converter     │
└─────────────────────────────────────────────────┘
         api (Request/Response/DTO/Remote接口)
                   types (枚举/常量/异常)
```

**依赖方向：** `adapter → application → domain ← infrastructure`，domain 不依赖任何外部框架。

### 聚合边界

```
SysUser (聚合根)
  ├── id: SysUserId
  ├── deptId: SysDeptId         ← 关联部门
  ├── tenantId: SysTenantId     ← 所属租户
  └── roleIds: List<SysRoleId>  ← 关联角色（聚合内）

SysRole (聚合根)
  ├── id: SysRoleId
  ├── tenantId: SysTenantId     ← 所属租户
  └── menuIds: List<SysMenuId>  ← 关联菜单（聚合内）

SysMenu / SysDept / SysTenant / SysArea 等 → 独立实体
```

### CQRS 模式

User 和 Role 聚合采用 CQRS 读写分离：

```
ReadRepository (查询侧)     WriteRepository (命令侧)
  queryPage(query)             create(entity)
  queryById(id)                update(entity)
                                removeBatchByIds(ids)
```

其余实体使用统一的 `IBaseSimpleRepository`（合并读写）。

### 服务间通信

```
kbpd-auth ──Dubbo──→ kbpd-upms
   │  IRemoteUserService.findByUsername()
   │  IRemoteUserService.loginByPwd()
   │  IRemoteOauthClientService.queryByClientId()
   │
kbpd-gateway ──HTTP──→ kbpd-upms
   │  SysRouteConfController (读取路由配置)
```

---

## 配置项

### application.yml

| 配置项 | 说明 | 示例 |
|---|---|---|
| `server.port` | 服务端口 | `:随Nacos配置` |
| `spring.application.name` | 服务名 | `kbpd-upms` |
| `spring.cloud.nacos.*` | Nacos 注册/配置 | — |
| `kbpd.upms.*` | UPMS 自定义配置 | `test: placeholder` |

### application-dev.yml

| 配置项 | 说明 |
|---|---|
| `spring.datasource.*` | MySQL 数据源（Druid 连接池） |
| `mybatis-plus.*` | MyBatis-Plus 配置 |
| `spring.data.redis.*` | Redis 连接 |

---

## 运行

```bash
# 前置条件：Nacos 已启动
cd kbpd-upms/kbpd-upms-bootstrap && mvn spring-boot:run
```

- 启动类：`com.kava.kbpd.upms.UpmsApplication`
- Dev 环境安全配置：`DevSecurityConfig` 放行所有请求（仅 dev profile）

---

## 当前开发状态

| 功能 | 状态 | 说明 |
|---|---|---|
| 用户 CRUD | ✅ | 基础增删改查已完成 |
| 角色 CRUD | ✅ | 含数据权限范围（dsType） |
| 菜单 CRUD | ✅ | 树形菜单、按钮权限 |
| 部门 CRUD | ✅ | 组织架构树 |
| 租户 CRUD | ✅ | 多租户管理 |
| 地区管理 | ✅ | 含树形查询（/tree） |
| 日志 CRUD | ✅ | 操作日志 |
| 审计日志 CRUD | ✅ | 字段变更审计 |
| 文件 / 文件组 CRUD | ✅ | 基础文件管理 |
| 国际化 CRUD | ✅ | zh/en 键值对 |
| 公共参数 CRUD | ✅ | 系统参数配置 |
| 路由配置 CRUD | ✅ | 动态网关路由 |
| OAuth 客户端 CRUD | ✅ | 客户端详情管理 |
| Dubbo RPC 接口 | ⚠️ | RemoteUserService 为桩实现（findByUsername 返回 null） |
| 用户-角色关联持久化 | ❌ | SysUserRolePO 存在但聚合内 roleIds 未持久化 |
| 角色-菜单关联持久化 | ❌ | SysRoleMenuPO 存在但聚合内 menuIds 未持久化 |
| 领域业务逻辑 | ❌ | 密码加密、锁定策略等未在领域层实现 |
| 错误码定义 | ❌ | UpmsBizErrorCodeEnum 为空 |
