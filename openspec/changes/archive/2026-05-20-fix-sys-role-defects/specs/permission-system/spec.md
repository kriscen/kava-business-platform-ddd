## ADDED Requirements

### Requirement: 角色查询租户隔离
角色分页查询时，系统 SHALL 根据当前用户的 tenantId 过滤结果，确保租户间数据隔离。

#### Scenario: 租户管理员查询角色列表
- **WHEN** 租户管理员请求角色分页列表
- **THEN** 系统从 Security Context 获取当前用户的 tenantId
- **AND** 仅返回属于该租户的角色记录

#### Scenario: 平台管理员查询角色列表
- **WHEN** 平台管理员（无 tenantId）请求角色分页列表
- **THEN** 系统返回所有租户的角色记录，不按租户过滤

### Requirement: roleCode 租户内唯一
同一租户内，角色标识 roleCode SHALL 保持唯一。不同租户间允许 roleCode 重复。

#### Scenario: 创建角色时 roleCode 重复
- **WHEN** 管理员创建角色，roleCode 与同租户下已有角色重复
- **THEN** 系统拒绝创建并返回唯一性冲突错误

#### Scenario: 更新角色时 roleCode 与其他角色冲突
- **WHEN** 管理员更新角色 roleCode，新 roleCode 与同租户下其他角色重复
- **THEN** 系统拒绝更新并返回唯一性冲突错误

#### Scenario: 不同租户 roleCode 相同
- **WHEN** 租户 A 和租户 B 各创建一个 roleCode 为 "admin" 的角色
- **THEN** 两个角色均可正常创建，互不影响

### Requirement: 角色列表 DTO 完整返回
角色分页查询接口 SHALL 返回完整的列表展示字段。

#### Scenario: 查询角色列表
- **WHEN** 管理员请求角色分页列表
- **THEN** 每条记录包含 id、roleName、roleCode、roleDesc、dsType、dsScope、gmtCreate、gmtModified

### Requirement: API 层审计字段清理
角色相关的 API 请求和响应对象 SHALL 遵循字段最小暴露原则。

#### Scenario: 创建/更新角色请求不含审计字段
- **WHEN** 管理员提交角色创建或更新请求
- **THEN** Request 对象仅包含业务字段（roleName、roleCode、roleDesc、dsType、dsScope、menuIds、id）
- **AND** 不包含 creator、gmtCreate、modifier、gmtModified、delFlag

#### Scenario: 角色详情响应仅保留时间审计字段
- **WHEN** 管理员查询角色详情
- **THEN** Response 包含 gmtCreate、gmtModified
- **AND** 不包含 creator、modifier、delFlag

#### Scenario: 角色列表响应不含删除标识
- **WHEN** 管理员查询角色列表
- **THEN** 列表 Response 不包含 delFlag 字段

### Requirement: 依赖注入使用构造器注入
Controller 层和 Infrastructure 层 SHALL 使用构造器注入，禁止使用 @Resource 或 @Autowired 字段注入。

#### Scenario: 角色相关 Bean 的依赖注入方式
- **WHEN** 容器初始化 SysRoleController 和 SysRoleReadRepository/SysRoleWriteRepository
- **THEN** 所有依赖通过构造器参数注入
- **AND** 不使用 @Resource 或 @Autowired 字段注入
