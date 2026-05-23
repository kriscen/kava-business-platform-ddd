## ADDED Requirements

### Requirement: OauthClient DomainService CRUD 委托

SysOauthClientService SHALL 注入 ISysOauthClientRepository，并将所有 CRUD 操作委托转发至 Repository。

#### Scenario: 创建 OAuth 客户端
- **WHEN** 调用 `SysOauthClientService.create(entity)`
- **THEN** 委托 `ISysOauthClientRepository.create(entity)` 并返回 `SysOauthClientId`

#### Scenario: 更新 OAuth 客户端
- **WHEN** 调用 `SysOauthClientService.update(entity)`
- **THEN** 委托 `ISysOauthClientRepository.update(entity)` 并返回 `Boolean`

#### Scenario: 分页查询 OAuth 客户端
- **WHEN** 调用 `SysOauthClientService.queryPage(query)`
- **THEN** 委托 `ISysOauthClientRepository.queryPage(query)` 并返回 `PagingInfo<SysOauthClientEntity>`

#### Scenario: 按ID查询 OAuth 客户端
- **WHEN** 调用 `SysOauthClientService.queryById(id)`
- **THEN** 委托 `ISysOauthClientRepository.queryById(id)` 并返回 `SysOauthClientEntity`

#### Scenario: 批量删除 OAuth 客户端
- **WHEN** 调用 `SysOauthClientService.removeBatchByIds(ids)`
- **THEN** 委托 `ISysOauthClientRepository.removeBatchByIds(ids)` 并返回 `Boolean`

### Requirement: OauthClient DomainService 暴露 queryByClientId 方法

ISysOauthClientService 接口 SHALL 提供 `queryByClientId(String)` 方法，委托 `ISysOauthClientRepository.queryByClientId(clientId)` 根据 clientId 查询客户端信息。

#### Scenario: 按 clientId 查询 OAuth 客户端
- **WHEN** 调用 `SysOauthClientService.queryByClientId(clientId)`
- **THEN** 委托 `ISysOauthClientRepository.queryByClientId(clientId)` 并返回匹配的 `SysOauthClientEntity`

#### Scenario: clientId 不存在
- **WHEN** 调用 `SysOauthClientService.queryByClientId(clientId)` 且 clientId 不存在
- **THEN** 返回 null
