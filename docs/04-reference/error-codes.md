# 错误码规范

## 格式定义

错误码为 **8 位数字字符串**，分三段：

```
  MM  SS  NNNN
  ──  ──  ────
  模块 子模块 具体错误
```

- **MM（2 位）**：大模块编号
- **SS（2 位）**：子模块编号（通用错误码中表示错误类别）
- **NNNN（4 位）**：具体错误序号，从 0001 递增

示例：`"10010001"` → UPMS(10) / 角色(01) / 角色未找到(0001)

## 模块号分配

| MM   | 模块         | 说明                   |
|------|-------------|------------------------|
| `00` | 通用         | 框架级、非业务           |
| `10` | UPMS        | 用户权限管理             |
| `20` | Member      | 会员                   |
| `30` | Auth        | 认证授权                |
| `40`-`99` | 预留    | 未来业务模块             |

## 通用错误码（MM = 00）

通用错误码用 SS 区分错误类别。

### 00-00 系统级

| 错误码       | 常量名                  | 说明           |
|-------------|-------------------------|----------------|
| `00000001`  | `SYSTEM_UNKNOWN_ERROR`  | 系统未知错误    |

### 00-01 客户端请求错误

| 错误码       | 常量名                             | 说明                     |
|-------------|-------------------------------------|--------------------------|
| `00010001`  | `CLIENT_HTTP_METHOD_ERROR`          | 请求方法不支持             |
| `00010002`  | `CLIENT_REQUEST_BODY_CHECK_ERROR`   | 请求体校验失败             |
| `00010003`  | `CLIENT_REQUEST_BODY_FORMAT_ERROR`  | 请求体 JSON 格式错误       |
| `00010004`  | `CLIENT_PATH_VARIABLE_ERROR`        | 路径参数类型错误           |
| `00010005`  | `CLIENT_REQUEST_PARAM_CHECK_ERROR`  | 请求参数校验失败           |
| `00010006`  | `CLIENT_REQUEST_PARAM_REQUIRED_ERROR`| 缺少必填参数             |

### 00-02 服务端错误

| 错误码       | 常量名                            | 说明                     |
|-------------|-------------------------------------|--------------------------|
| `00020001`  | `SERVER_ILLEGAL_ARGUMENT_ERROR`     | 业务参数校验失败           |
| `00020002`  | `SERVER_DATABASE_ERROR`             | 数据库操作异常             |
| `00020003`  | `SERVER_RPC_ERROR`                  | RPC 调用异常              |

### 00-03 认证/授权错误

| 错误码       | 常量名                  | 说明                     |
|-------------|-------------------------|--------------------------|
| `00030001`  | `AUTH_UNAUTHORIZED`     | 未认证（缺少 token）      |
| `00030002`  | `AUTH_TOKEN_EXPIRED`    | token 已过期              |
| `00030003`  | `AUTH_TOKEN_INVALID`    | token 无效                |
| `00030004`  | `AUTH_FORBIDDEN`        | 权限不足                  |
| `00030005`  | `AUTH_TENANT_INVALID`   | 租户不存在或已禁用         |

## UPMS 业务错误码（MM = 10）

### 子模块分配

| SS   | 子模块            | 说明                |
|------|------------------|---------------------|
| `01` | 角色（Role）       |                     |
| `02` | 用户（User）       |                     |
| `03` | 菜单（Menu）       |                     |
| `04` | 租户（Tenant）     |                     |
| `05` | 分组（Group）       |                     |
| `06` | OAuth 客户端       |                     |
| `08` | 国际化（I18n）     |                     |
| `09` | 应用（App）        |                     |
| `10` | 租户应用（TenantApp） |                   |

### 10-01 角色

| 错误码       | 常量名                 | 说明                       |
|-------------|------------------------|----------------------------|
| `10010001`  | `ROLE_NOT_FOUND`       | 角色未找到                  |
| `10010002`  | `ROLE_CODE_DUPLICATE`  | 角色编码已存在               |
| `10010003`  | `ROLE_MENU_EMPTY`      | 角色必须关联至少一个菜单      |

### 10-02 用户

| 错误码       | 常量名                    | 说明           |
|-------------|---------------------------|----------------|
| `10020001`  | `USER_NOT_FOUND`          | 用户未找到      |
| `10020002`  | `USER_USERNAME_DUPLICATE` | 用户名已存在    |

### 10-03 菜单

| 错误码       | 常量名                    | 说明                        |
|-------------|---------------------------|-----------------------------|
| `10030001`  | `MENU_NOT_FOUND`          | 菜单未找到                   |
| `10030002`  | `MENU_SCOPE_INVALID`      | 菜单权限标识无效              |
| `10030003`  | `MENU_PID_SELF_REFERENCE` | 上级菜单不能是自身             |
| `10030004`  | `MENU_PID_CIRCULAR`       | 上级菜单不能形成循环引用       |
| `10030005`  | `MENU_HAS_CHILDREN`       | 菜单存在子菜单，不可删除       |
| `10030006`  | `MENU_REFERENCED_BY_ROLE` | 菜单被角色引用，不可删除       |

### 10-04 租户

| 错误码       | 常量名                            | 说明                 |
|-------------|-------------------------------------|----------------------|
| `10040001`  | `TENANT_NOT_FOUND`                  | 租户未找到            |
| `10040002`  | `TENANT_CODE_DUPLICATE`             | 租户编码已存在         |
| `10040003`  | `TENANT_STATUS_INVALID_TRANSITION`  | 租户状态流转不合法     |

### 10-05 分组

| 错误码       | 常量名                     | 说明                        |
|-------------|----------------------------|------------------------------|
| `10050001`  | `GROUP_PID_SELF_REFERENCE` | 上级分组不能是自身             |
| `10050002`  | `GROUP_PID_CIRCULAR`       | 上级分组不能形成循环引用       |
| `10050003`  | `GROUP_HAS_CHILDREN`       | 分组存在子分组，不可删除       |
| `10050004`  | `GROUP_REFERENCED_BY_USER` | 分组被用户引用，不可删除       |

### 10-06 OAuth 客户端

| 错误码       | 常量名                        | 说明                 |
|-------------|-------------------------------|----------------------|
| `10060001`  | `CLIENT_ID_DUPLICATE`         | 客户端 ID 已存在      |
| `10060002`  | `CLIENT_SECRET_REQUIRED`      | 客户端密钥不能为空    |
| `10060003`  | `CLIENT_TOKEN_VALIDITY_INVALID`| token 有效期无效     |
| `10060004`  | `CLIENT_GRANT_TYPES_REQUIRED` | 授权类型不能为空      |

### 10-08 国际化

| 错误码       | 常量名              | 说明                 |
|-------------|---------------------|----------------------|
| `10080001`  | `I18N_CODE_DUPLICATE`| 国际化编码已存在      |

### 10-09 应用

| 错误码       | 常量名                 | 说明                 |
|-------------|------------------------|----------------------|
| `10090001`  | `APP_NOT_FOUND`        | 应用不存在            |
| `10090002`  | `APP_CODE_DUPLICATE`   | 应用编码已存在         |
| `10090003`  | `APP_NOT_DELETABLE`    | 系统应用不可删除       |
| `10090004`  | `APP_NOT_DISABLEABLE`  | 系统应用不可停用       |
| `10090005`  | `APP_IN_USE`           | 应用仍有租户使用，无法删除 |
| `10090006`  | `APP_DISABLED`         | 应用已停用，无法订阅    |

### 10-10 租户应用

| 错误码       | 常量名                        | 说明                 |
|-------------|-------------------------------|----------------------|
| `10100001`  | `TENANT_APP_ALREADY_SUBSCRIBED`| 租户已订阅该应用      |
| `10100002`  | `TENANT_APP_NOT_UNSUBSCRIBABLE`| kava-base 系统应用不可退订 |
| `10100003`  | `TENANT_APP_MENU_OUT_OF_SCOPE` | 菜单超出可分配范围    |
| `10100004`  | `APP_MENU_NOT_FOUND`           | 关联的菜单不存在      |

## 预留模块示例

```
20-01-xxxx  会员 - 会员信息
20-02-xxxx  会员 - 会员等级
30-01-xxxx  Auth - 登录
30-02-xxxx  Auth - Token
```

## 前端使用指南

### 响应格式

所有接口统一返回：

```json
{
  "success": true,
  "data": { ... },
  "errorCode": null,
  "errorMessage": null
}
```

异常时：

```json
{
  "success": false,
  "data": null,
  "errorCode": "10010001",
  "errorMessage": "Role not found"
}
```

### 前端错误码解析建议

```javascript
// 按段解析错误码
function parseErrorCode(code) {
  const module = code.substring(0, 2)     // "10" → UPMS
  const subModule = code.substring(2, 4)  // "01" → 角色
  const error = code.substring(4, 8)      // "0001" → 未找到
  return { module, subModule, error }
}

// 判断错误类型
function isClientError(code) {
  return code.startsWith('0001')  // 00-01-xxxx
}

function isAuthError(code) {
  return code.startsWith('0003')  // 00-03-xxxx
}

function isServerError(code) {
  return code.startsWith('0002')  // 00-02-xxxx
}
```
