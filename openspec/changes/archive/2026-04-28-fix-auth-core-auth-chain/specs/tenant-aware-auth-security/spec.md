## ADDED Requirements

### Requirement: TenantAwareAuthenticationFilter 从 ClientSettings 获取可信租户参数

`TenantAwareAuthenticationFilter` MUST 通过 `RegisteredClientRepository` 查询已注册的 OAuth2 Client，从 `ClientSettings` 中获取可信的 tenantId 和 userType，不得信任前端表单提交的 tenantId 和 userType 参数。

#### Scenario: 使用 ClientSettings 中的可信参数构造认证 Token
- **WHEN** 用户提交登录表单（POST /oauth2/login），包含 username、password、clientId
- **THEN** Filter MUST 使用 clientId 调用 `RegisteredClientRepository.findByClientId(clientId)` 查询 Client 配置
- **AND** 从 ClientSettings 中提取 tenantId 和 userType
- **AND** 使用服务端获取的 tenantId 和 userType 构造 `ExtendAuthenticationToken`

#### Scenario: clientId 对应的 Client 不存在
- **WHEN** 用户提交登录表单中的 clientId 在已注册客户端中不存在
- **THEN** MUST 抛出 `AuthenticationServiceException`
- **AND** 不得继续认证流程

#### Scenario: 前端篡改 tenantId 不影响认证结果
- **WHEN** 攻击者修改前端表单中的 tenantId 为其他租户的 ID
- **THEN** 认证流程 MUST 忽略前端提交的 tenantId
- **AND** 使用 ClientSettings 中的 tenantId 完成认证

#### Scenario: 前端篡改 userType 不影响认证结果
- **WHEN** 攻击者修改前端表单中的 userType（如将 C端用户改为 B端）
- **THEN** 认证流程 MUST 忽略前端提交的 userType
- **AND** 使用 ClientSettings 中的 userType 完成认证

### Requirement: DBRegisteredClientRepository.findById 不抛异常

`DBRegisteredClientRepository.findById(id)` MUST 返回有效的 `RegisteredClient`，不得抛出 `UnsupportedOperationException`。

#### Scenario: findById 返回正确的 RegisteredClient
- **WHEN** Spring Authorization Server 内部调用 `findById(id)`
- **THEN** MUST 返回与 `findByClientId(id)` 相同的结果
- **AND** 不得抛出任何异常
