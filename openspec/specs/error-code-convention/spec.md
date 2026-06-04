## Purpose

定义全平台统一的错误码格式规范，确保错误码无冲突、可追溯，并规范全局异常处理器的返回格式。

## Requirements

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

### Requirement: 通用错误码定义

`CommonErrorCodeEnum` MUST 包含以下错误码分区：
- `00-00-xxxx`：系统级错误（系统未知错误等）
- `00-01-xxxx`：客户端请求错误（请求方法、请求体、参数等）
- `00-02-xxxx`：服务端错误（参数校验、数据库、RPC 等）
- `00-03-xxxx`：认证/授权错误（未认证、token 过期、权限不足等）

#### Scenario: 系统未知错误码
- **WHEN** 系统发生未预期的异常
- **THEN** `CommonErrorCodeEnum.SYSTEM_UNKNOWN_ERROR` 的码值 MUST 为 `"00000001"`

#### Scenario: 客户端请求错误码范围
- **WHEN** 客户端发送了不支持的 HTTP 方法
- **THEN** `CommonErrorCodeEnum.CLIENT_HTTP_METHOD_ERROR` 的码值 MUST 为 `"00010001"`

#### Scenario: 认证错误码定义
- **WHEN** 检查认证/授权错误码
- **THEN** `CommonErrorCodeEnum` MUST 包含 `AUTH_UNAUTHORIZED`（`"00030001"`）、`AUTH_TOKEN_EXPIRED`（`"00030002"`）、`AUTH_TOKEN_INVALID`（`"00030003"`）、`AUTH_FORBIDDEN`（`"00030004"`）、`AUTH_TENANT_INVALID`（`"00030005"`）

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

#### Scenario: 码值冲突已修复
- **WHEN** 检查 `ROLE_MENU_EMPTY` 和 `MENU_NOT_FOUND` 的码值
- **THEN** 两者 MUST 不同，`ROLE_MENU_EMPTY` 为 `"10010003"`，`MENU_NOT_FOUND` 为 `"10030001"`

#### Scenario: 应用子模块错误码
- **WHEN** 查询不存在的应用
- **THEN** `UpmsBizErrorCodeEnum.APP_NOT_FOUND` 的码值 MUST 为 `"10090001"`

#### Scenario: 租户应用订阅子模块错误码
- **WHEN** 租户重复订阅同一应用
- **THEN** `UpmsBizErrorCodeEnum.TENANT_APP_ALREADY_SUBSCRIBED` 的码值 MUST 为 `"10100001"`

### Requirement: 全局异常处理器统一返回 JsonResult

系统 MUST 提供 `@RestControllerAdvice` 全局异常处理器，确保所有 HTTP 异常响应符合 `JsonResult` 格式。

#### Scenario: 业务异常返回 JsonResult
- **WHEN** Controller 抛出 `BaseBizException`
- **THEN** 响应状态码 MUST 为 200，响应体 MUST 为 `{"success": false, "data": null, "errorCode": "<错误码>", "errorMessage": "<错误信息>"}`

#### Scenario: 参数校验异常返回 JsonResult
- **WHEN** 请求参数校验失败（`MethodArgumentNotValidException`）
- **THEN** 响应体 MUST 为 JsonResult 格式，errorCode 为 `"00010002"`，errorMessage 包含校验失败的字段信息

#### Scenario: 请求方法不支持返回 JsonResult
- **WHEN** 客户端使用了不支持的 HTTP 方法（`HttpRequestMethodNotSupportedException`）
- **THEN** 响应体 MUST 为 JsonResult 格式，errorCode 为 `"00010001"`

#### Scenario: 未捕获异常返回 JsonResult
- **WHEN** 系统抛出未预期的 `Exception`
- **THEN** 响应体 MUST 为 JsonResult 格式，errorCode 为 `"00000001"`，errorMessage 为 `"System unknown error"`

### Requirement: 已知 bug 修复

#### Scenario: AuthenticationFailureEventHandler 正确返回错误码
- **WHEN** OAuth2 认证失败
- **THEN** `AuthenticationFailureEventHandler` MUST 调用 `JsonResult.buildError(error.getErrorCode(), error.getDescription())` 而非 `JsonResult.buildError(error.getErrorCode())`

#### Scenario: SysI18nAppService 使用枚举错误码
- **WHEN** 创建重复的国际化编码
- **THEN** `SysI18nAppService` MUST 抛出 `UpmsBizException(UpmsBizErrorCodeEnum.I18N_CODE_DUPLICATE)` 而非使用硬编码字符串
