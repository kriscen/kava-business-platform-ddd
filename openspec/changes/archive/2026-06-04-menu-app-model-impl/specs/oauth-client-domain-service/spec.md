## MODIFIED Requirements

### Requirement: OauthClient DomainService CRUD 委托

SysOauthClientService SHALL 注入 ISysOauthClientRepository，将 CRUD 操作委托转发至 Repository，并在创建和更新时执行业务校验。SysOauthClientEntity 可选关联 `SysAppId`，标记该 Client 属于哪个 App 的租户实例。

#### Scenario: 创建 OAuth 客户端
- **WHEN** 调用 `SysOauthClientService.create(entity)`
- **THEN** 先校验 clientId 唯一性（不存在相同 clientId 的记录）、clientSecret 非空、accessTokenValidity 和 refreshTokenValidity 范围（大于 0）、authorizedGrantTypes 非空
- **AND** 校验通过后委托 `ISysOauthClientRepository.create(entity)` 并返回 `SysOauthClientId`

#### Scenario: 创建时 clientId 已存在
- **WHEN** 调用 `SysOauthClientService.create(entity)` 且 clientId 已存在
- **THEN** 抛出 `UpmsBizException(CLIENT_ID_DUPLICATE)`

#### Scenario: 创建时 clientSecret 为空
- **WHEN** 调用 `SysOauthClientService.create(entity)` 且 clientSecret 为 null 或空字符串
- **THEN** 抛出 `UpmsBizException(CLIENT_SECRET_REQUIRED)`

#### Scenario: 创建时 token 有效期无效
- **WHEN** 调用 `SysOauthClientService.create(entity)` 且 accessTokenValidity 或 refreshTokenValidity 为 null 或小于等于 0
- **THEN** 抛出 `UpmsBizException(CLIENT_TOKEN_VALIDITY_INVALID)`

#### Scenario: 创建时 authorizedGrantTypes 为空
- **WHEN** 调用 `SysOauthClientService.create(entity)` 且 authorizedGrantTypes 为 null 或空数组
- **THEN** 抛出 `UpmsBizException(CLIENT_GRANT_TYPES_REQUIRED)`

#### Scenario: 创建 OAuth 客户端时关联 App
- **WHEN** 调用 `SysOauthClientService.create(entity)` 且 entity.appId 非空
- **THEN** 系统校验该 App 存在且状态为 ACTIVE
- **AND** 创建 Client 时记录 appId 关联

#### Scenario: 创建 OAuth 客户端时不关联 App
- **WHEN** 调用 `SysOauthClientService.create(entity)` 且 entity.appId 为 null
- **THEN** 系统正常创建 Client，不校验 App
- **AND** B 端管理后台的 Client 可不关联 App

#### Scenario: 更新 OAuth 客户端
- **WHEN** 调用 `SysOauthClientService.update(entity)`
- **THEN** 若 clientId 发生变更，校验新 clientId 唯一性；若 clientSecret 非空则校验；校验 token 有效期和 authorizedGrantTypes
- **AND** 校验通过后委托 `ISysOauthClientRepository.update(entity)` 并返回 `Boolean`

#### Scenario: 分页查询 OAuth 客户端
- **WHEN** 调用 `SysOauthClientService.queryPage(query)`
- **THEN** 委托 `ISysOauthClientRepository.queryPage(query)` 并返回 `PagingInfo<SysOauthClientEntity>`

#### Scenario: 按ID查询 OAuth 客户端
- **WHEN** 调用 `SysOauthClientService.queryById(id)`
- **THEN** 委托 `ISysOauthClientRepository.queryById(id)` 并返回 `SysOauthClientEntity`

#### Scenario: 批量删除 OAuth 客户端
- **WHEN** 调用 `SysOauthClientService.removeBatchByIds(ids)`
- **THEN** 委托 `ISysOauthClientRepository.removeBatchByIds(ids)` 并返回 `Boolean`
