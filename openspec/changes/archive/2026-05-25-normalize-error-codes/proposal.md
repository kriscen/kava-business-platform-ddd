## Why

当前项目错误码格式不统一：通用错误码使用 4 位纯数字（`1001`、`2001`），业务错误码使用字母+5 位（`A00101`），无法一眼识别错误归属模块。此外存在码值冲突（`A00301` 同时用于角色和菜单）、硬编码字符串错误码、以及缺少全局异常处理器导致未捕获异常不走 JsonResult 格式等问题。前端即将对接，需要统一的错误码体系作为契约基础。

## What Changes

- **BREAKING**: 重新定义 `CommonErrorCodeEnum` 所有码值，从 4 位数字改为 8 位格式（`00-xx-nnnn`）
- **BREAKING**: 重新定义 `UpmsBizErrorCodeEnum` 所有码值，从 `Axxxxx` 改为 8 位格式（`10-ss-nnnn`）
- 修复 `ROLE_MENU_EMPTY` 与 `MENU_NOT_FOUND` 的码值冲突
- 修复 `SysI18nAppService` 中硬编码的 `"I18N_CODE_DUPLICATE"` 字符串
- 修复 `AuthenticationFailureEventHandler` 中 `buildError` 调用参数错误的 bug
- 新增全局异常处理器 `GlobalExceptionHandler`（`@RestControllerAdvice`），统一捕获异常并返回 JsonResult
- 新增通用认证/授权错误码（`00-03-xxxx`）和服务端错误码（`00-02-xxxx` 的补充项）

## Capabilities

### New Capabilities

- `error-code-convention`: 统一错误码格式规范（8 位数字字符串 MM-SS-NNNN），包含通用错误码和业务错误码的编码规则、模块号分配、前端使用指南

### Modified Capabilities

（无已有 spec 需要修改）

## Impact

**受影响模块（按变更顺序）：**

1. **kbpd-common-core** — `CommonErrorCodeEnum` 码值重写、`BaseErrorCodeEnum` 接口不变、`BaseBizException` 不变
2. **kbpd-common-web** — 新增 `GlobalExceptionHandler`（`@RestControllerAdvice`）
3. **kbpd-upms-types** — `UpmsBizErrorCodeEnum` 码值重写、`UpmsBizException` 不变
4. **kbpd-auth** — `AuthenticationFailureEventHandler` bug 修复
5. **kbpd-upms-application** — `SysI18nAppService` 硬编码修正

**不受影响：**
- Controller 层（异常处理器自动覆盖）
- Domain 层（异常类接口不变，只是码值变化）
- 数据库（错误码不持久化）
- 前端（尚未对接，无兼容性问题）
