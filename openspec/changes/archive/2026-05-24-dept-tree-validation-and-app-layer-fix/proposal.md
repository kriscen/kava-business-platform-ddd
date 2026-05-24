## Why

SysDept 部门模块存在三个结构性缺陷：（1）`sys_dept` 表有 `pid` 字段表示父子层级，但 Domain 层完全缺失树形验证规则（自引用、循环引用、删除前约束检查），可能导致脏数据；（2）Application 层的 CreateCommand、UpdateCommand、DetailDTO、ListDTO 均为空类，导致 CRUD 操作无法正确传递和返回数据；（3）AppService 绕过 DomainService 直连 Repository，使得新增的树形验证规则无法被触发。

## What Changes

- 在 DomainService `SysDeptService` 中增加树形验证规则：创建/更新时校验 pid 自引用和循环引用，删除前校验子节点和用户引用
- 在 `ISysDeptRepository` 中新增 `queryAll()`、`queryByPid()`、`existsUserReference()` 方法，并在 Infrastructure 层实现
- 在 `UpmsBizErrorCodeEnum` 中新增 dept 专属错误码（DEPT_PID_SELF_REFERENCE、DEPT_PID_CIRCULAR、DEPT_HAS_CHILDREN、DEPT_REFERENCED_BY_USER）
- 填充 Application 层空壳类：`SysDeptCreateCommand`（name、sortOrder、pid）、`SysDeptUpdateCommand`（id、name、sortOrder、pid）、`SysDeptAppDetailDTO`（id、name、sortOrder、pid、tenantId）、`SysDeptAppListDTO`（id、name、sortOrder、pid）
- 修正 `SysDeptAppService` 从直连 `ISysDeptRepository` 改为注入 `ISysDeptService`，走 DomainService 调用链
- 确保 `queryAll()` 查询受租户隔离过滤

## Capabilities

### New Capabilities

- `dept-tree-validation`: 部门树形结构验证规则——自引用检查、循环引用检查、删除前子节点检查、删除前用户引用检查

### Modified Capabilities

- `dept-domain-service`: 从纯 CRUD 委托扩展为带树形验证的 DomainService；Repository 接口新增 `queryAll()`、`queryByPid()`、`existsUserReference()`

## Impact

- `kbpd-upms-domain`: SysDeptService（增加验证逻辑）、ISysDeptRepository（新增方法）、ISysDeptService（方法签名不变）
- `kbpd-upms-infrastructure`: SysDeptRepository（实现新方法，注入 SysUserMapper 检查用户引用）
- `kbpd-upms-types`: UpmsBizErrorCodeEnum（新增 4 个 dept 错误码）
- `kbpd-upms-application`: SysDeptCreateCommand、SysDeptUpdateCommand、SysDeptAppDetailDTO、SysDeptAppListDTO（填充字段）；SysDeptAppService（改注入 ISysDeptService）；SysDeptAppConverter（适配新字段）
