## Context

当前项目有两套错误码格式：`CommonErrorCodeEnum` 使用 4 位纯数字（`-1`、`1001`、`2001`），`UpmsBizErrorCodeEnum` 使用字母+5 位（`A00101`）。两者缺乏统一规范，无法从错误码识别归属模块。此外缺少 `@RestControllerAdvice` 全局异常处理器，未捕获异常不走 JsonResult 格式，前端无法统一处理。

## Goals / Non-Goals

**Goals:**

- 建立统一的 8 位数字字符串错误码格式（`MM-SS-NNNN`）
- 迁移现有错误码到新格式
- 新增全局异常处理器，确保所有异常返回 JsonResult
- 修复已知 bug（码值冲突、硬编码、参数错误）

**Non-Goals:**

- 不改变 `BaseErrorCodeEnum` 接口定义
- 不改变 `BaseBizException` / `UpmsBizException` 异常类结构
- 不新增前端需要的认证/授权错误码的实际处理逻辑（仅定义码值）
- 不改造 Gateway 层的异常处理
- 不为 Member 模块定义业务错误码（Member 尚无业务实现）

## Decisions

### 决策 1：错误码格式为 8 位数字字符串

格式：`MM-SS-NNNN`，其中 MM=模块、SS=子模块、NNNN=具体错误序号。

**Because**: 数字字符串兼顾可读性和解析便利性；8 位在 int 范围内（前端无精度问题但保持 String 以支持未来扩展）；分段结构允许按前缀聚合错误类型。

**替代方案**: 保持字母前缀（如 `UPMS_ROLE_001`）——可读性更好但不利于数字比较和前端 i18n key 映射。

### 决策 2：通用错误码的 SS 段表示错误类别

通用码（MM=00）的 SS 段按错误类别划分：00=系统级、01=客户端请求、02=服务端、03=认证授权。

**Because**: 通用错误码没有"子模块"概念，复用 SS 段作为分类维度，保持格式一致的同时语义清晰。

### 决策 3：全局异常处理器放在 kbpd-common-web

新增 `GlobalExceptionHandler` 类放在 `kbpd-common-web` 模块，所有业务模块通过依赖 `kbpd-common-web` 自动获得异常处理能力。

**Because**: `kbpd-common-web` 已经是 web 层的共享模块（包含 `WebConfig`、`JsonResult` 等），全局异常处理器属于 web 层基础设施，放在此处符合依赖方向（adapter → common-web）。

**替代方案**: 放在 `kbpd-common-core`——但 core 不应依赖 Spring Web 注解。

### 决策 4：直接替换码值，不做兼容

前端尚未对接，直接使用新码值替换旧码值。

**Because**: 无存量前端解析旧码值，兼容层只会增加代码复杂度。如果未来需要支持多版本码值，可通过 i18n 机制实现。

### 决策 5：认证/授权错误码放在通用码（00-03）

401/403 相关错误码放在 `00-03-xxxx`，作为通用错误码的一部分。

**Because**: 认证授权是基础设施层逻辑（`kbpd-common-security`），不属于任何业务模块。所有模块共享同一套认证机制，错误码也应统一。

## Risks / Trade-offs

- **[风险] 旧码值引用遗漏** → 全局搜索 `CommonErrorCodeEnum` 和 `UpmsBizErrorCodeEnum` 的所有引用点，逐一替换。编译器会在常量名不变的情况下保证类型安全。
- **[风险] 全局异常处理器可能吞掉需要特殊处理的异常** → 只捕获 `BaseBizException` 和标准 Spring 异常，保留 `Exception` 兜底但记录 ERROR 日志。对于需要特殊响应格式的场景（如 Auth 的 OAuth2 错误），不走全局处理器。
- **[权衡] 通用码新增的 00-02 和 00-03 码值当前无引用** → 预定义码值为全局异常处理器和后续模块提供契约，避免频繁修改通用码枚举。
