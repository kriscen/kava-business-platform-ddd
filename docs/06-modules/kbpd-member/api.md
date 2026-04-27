# kbpd-member -- API 接口文档

当前为早期脚手架阶段，仅定义了 Dubbo RPC 接口骨架，HTTP REST 接口尚未实现。

---

## Dubbo RPC 接口

### IRemoteMemberService

- **包路径：** `com.kava.kbpd.member.api.service.IRemoteMemberService`
- **Dubbo 版本：** `1.0`
- **用途：** 供其他微服务（如 `kbpd-auth`）通过 Dubbo RPC 查询会员信息

#### 方法

| 方法签名 | 参数 | 返回值 | 说明 |
|---------|------|--------|------|
| `findMemberByMobile(String tenantId, String mobile)` | `tenantId` - 租户 ID，`mobile` - 手机号 | `MemberInfoDTO` | 根据租户 ID 和手机号查询会员信息 |

> **当前状态：** 桩实现，返回硬编码的 `MemberInfoDTO`（`id=1`，roles 和 permissions 为空）。待领域层实现后替换为真实查询。

---

## DTO 定义

### MemberInfoDTO

- **包路径：** `com.kava.kbpd.member.api.model.dto.MemberInfoDTO`
- **实现：** `Serializable`，Lombok `@Data`

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | `Long` | 会员 ID |
| `permissions` | `List<String>` | 权限标识列表 |
| `roles` | `List<String>` | 角色标识列表 |

---

## HTTP REST 接口（规划）

以下目录已预留但尚未实现：

| 路径 | 用途 | 状态 |
|------|------|------|
| `trigger/http/admin/` | 后台管理端 Controller | ❌ 空 |
| `trigger/http/app/` | 移动端/App Controller | ❌ 空 |

---

## 接口规划（待实现）

基于会员服务的定位，预期未来需实现以下接口：

### RPC 接口扩展

| 方法 | 说明 | 优先级 |
|------|------|--------|
| `findMemberById(Long id)` | 根据 ID 查询会员信息 | 高 |
| `findMemberByOpenId(String openId)` | 根据微信 OpenID 查询 | 中 |
| `registerMember(...)` | 会员注册 | 高 |

### HTTP Admin 接口（规划）

| 路径 | 方法 | 说明 |
|------|------|------|
| `/admin/v1/members` | GET | 分页查询会员列表 |
| `/admin/v1/members/{id}` | GET | 查询会员详情 |
| `/admin/v1/members/{id}/status` | PUT | 修改会员状态 |

### HTTP App 接口（规划）

| 路径 | 方法 | 说明 |
|------|------|------|
| `/app/v1/member/profile` | GET | 获取当前会员信息 |
| `/app/v1/member/profile` | PUT | 更新会员信息 |

> 以上为初步规划，实际接口设计需在领域模型确定后细化。
