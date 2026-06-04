## MODIFIED Requirements

### Requirement: 错误码格式为 8 位数字字符串
所有错误码 MUST 为 8 位数字字符串，格式为 `MM-SS-NNNN`：
- MM（2 位）：大模块编号，`00` 为通用，业务模块从 `10` 开始
- SS（2 位）：子模块编号，通用错误码中表示错误类别
- NNNN（4 位）：具体错误序号，从 `0001` 递增

#### Scenario: 通用错误码格式
- **WHEN** 系统抛出客户端请求方法不支持的异常
- **THEN** 错误码 MUST 为 `"00010001"`，格式符合 `00-01-NNNN`

#### Scenario: UPMS 业务错误码格式
- **WHEN** 系统抛出角色未找到的异常
- **THEN** 错误码 MUST 为 `"10010001"`，格式符合 `10-01-NNNN`

#### Scenario: 错误码无码值冲突
- **WHEN** 遍历 `CommonErrorCodeEnum` 和 `UpmsBizErrorCodeEnum` 的所有枚举值
- **THEN** 任意两个枚举值的 `errorCode` MUST 不相同

#### Scenario: 文档与代码格式一致
- **WHEN** `docs/04-reference/error-codes.md` 描述错误码格式
- **THEN** MUST 使用 8 位数字格式（如 `10010001`）
- **AND** `docs/06-modules/kbpd-upms/business-rules.md` 中的错误码表 MUST 使用相同的 8 位数字格式
- **AND** MUST NOT 使用简写格式（如 `A00101`）

### Requirement: UPMS 业务错误码定义

`UpmsBizErrorCodeEnum` MUST 按子模块分区，每个子模块使用独立的 SS 编号：
- `10-01`：角色（Role）
- `10-02`：用户（User）
- `10-03`：菜单（Menu）
- `10-04`：租户（Tenant）
- `10-05`：分组（Group）
- `10-06`：OAuth 客户端（Client）
- `10-08`：国际化（I18n）
- `10-09`：应用（App）
- `10-10`：租户应用订阅（TenantApp）

#### Scenario: 角色子模块错误码
- **WHEN** 查询不存在的角色
- **THEN** `UpmsBizErrorCodeEnum.ROLE_NOT_FOUND` 的码值 MUST 为 `"10010001"`

#### Scenario: 菜单子模块错误码
- **WHEN** 菜单的上级菜单设置为自身
- **THEN** `UpmsBizErrorCodeEnum.MENU_PID_SELF_REFERENCE` 的码值 MUST 为 `"10030003"`

#### Scenario: 应用子模块错误码
- **WHEN** 查询不存在的应用
- **THEN** `UpmsBizErrorCodeEnum.APP_NOT_FOUND` 的码值 MUST 为 `"10090001"`

#### Scenario: 租户应用订阅子模块错误码
- **WHEN** 租户重复订阅同一应用
- **THEN** `UpmsBizErrorCodeEnum.TENANT_APP_ALREADY_SUBSCRIBED` 的码值 MUST 为 `"10100001"`
