## 1. 通用错误码重构（kbpd-common-core）

- [x] 1.1 重写 `CommonErrorCodeEnum`：将所有码值迁移到 8 位格式（`00-xx-nnnn`），新增 `00-02` 服务端错误码和 `00-03` 认证授权错误码
- [x] 1.2 验证 `BaseErrorCodeEnum` 接口和 `BaseBizException` 类无需修改（接口契约不变）

## 2. 全局异常处理器（kbpd-common-web）

- [x] 2.1 新建 `GlobalExceptionHandler`（`@RestControllerAdvice`），捕获 `BaseBizException` 并返回 `JsonResult`
- [x] 2.2 补充 Spring 标准异常处理：`MethodArgumentNotValidException`、`HttpRequestMethodNotSupportedException`、`MissingServletRequestParameterException`、`HttpMessageNotReadableException`
- [x] 2.3 补充 `Exception` 兜底处理：返回 `SYSTEM_UNKNOWN_ERROR` 码值，记录 ERROR 日志

## 3. UPMS 业务错误码重构（kbpd-upms-types）

- [x] 3.1 重写 `UpmsBizErrorCodeEnum`：将所有 `Axxxxx` 码值迁移到 8 位格式（`10-ss-nnnn`），修复 `ROLE_MENU_EMPTY` 与 `MENU_NOT_FOUND` 的码值冲突
- [x] 3.2 验证 `UpmsBizException` 类无需修改（构造器接收 `BaseErrorCodeEnum`，不感知码值格式）

## 4. Bug 修复

- [x] 4.1 修复 `AuthenticationFailureEventHandler`：将 `buildError(error.getErrorCode())` 改为 `buildError(error.getErrorCode(), error.getDescription())`
- [x] 4.2 修复 `SysI18nAppService`：将硬编码 `"I18N_CODE_DUPLICATE"` 替换为 `UpmsBizErrorCodeEnum.I18N_CODE_DUPLICATE`

## 5. 验证

- [x] 5.1 全项目编译通过（`mvn clean install -DskipTests`）
- [x] 5.2 UPMS 模块测试通过（`mvn test -pl kbpd-upms/kbpd-upms-bootstrap`）
- [x] 5.3 验证 `CommonErrorCodeEnum` 和 `UpmsBizErrorCodeEnum` 无码值冲突
