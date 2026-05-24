## MODIFIED Requirements

### Requirement: OauthClient DomainService CRUD 委托

SysOauthClientService SHALL 注入 ISysOauthClientRepository，将 CRUD 操作委托转发至 Repository，并在创建和更新时执行业务校验。

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

### Requirement: OauthClient AppService 通过 DomainService 调用

SysOauthClientAppService SHALL 注入 ISysOauthClientService（而非 ISysOauthClientRepository），所有操作通过 DomainService 间接调用 Repository。

#### Scenario: AppService 不直接注入 Repository
- **WHEN** 查看 `SysOauthClientAppService` 的依赖注入
- **THEN** 不存在 `ISysOauthClientRepository` 字段
- **AND** 存在 `ISysOauthClientService` 字段

#### Scenario: AppService 创建操作调用链路
- **WHEN** 调用 `SysOauthClientAppService.createOauthClient(command)`
- **THEN** 转换 command 为 entity 后调用 `sysOauthClientService.create(entity)`
- **AND** 不直接调用 `sysOauthClientRepository.create()`

## ADDED Requirements

### Requirement: OauthClient AppService 写操作事务保障

SysOauthClientAppService 的写操作方法（create、update、remove）MUST 添加 `@Transactional(rollbackFor = Exception.class)` 注解。

#### Scenario: 创建操作有事务保障
- **WHEN** 调用 `SysOauthClientAppService.createOauthClient(command)`
- **THEN** 该方法在事务内执行，异常时自动回滚
