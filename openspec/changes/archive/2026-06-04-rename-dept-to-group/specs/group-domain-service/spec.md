## RENAMED Requirements

### FROM: group-domain-service → TO: group-domain-service

整个 capability 从 `group-domain-service` 重命名为 `group-domain-service`。所有标识符替换规则：

- `SysGroup*` → `SysGroup*`（类名、接口名、变量名）
- `ISysGroupRepository` → `ISysGroupRepository`
- `SysGroupId` → `SysGroupId`
- `ISysGroupService` → `ISysGroupService`
- `SysGroupService` → `SysGroupService`
- `SysGroupEntity` → `SysGroupEntity`
- `GROUP_PID_SELF_REFERENCE` → `GROUP_PID_SELF_REFERENCE`
- `GROUP_PID_CIRCULAR` → `GROUP_PID_CIRCULAR`
- `GROUP_HAS_CHILDREN` → `GROUP_HAS_CHILDREN`
- `GROUP_REFERENCED_BY_USER` → `GROUP_REFERENCED_BY_USER`
- `sys_dept` → `sys_group`（表名）
- `dept_id` → `group_id`（列名）
- API 路径 `/api/v1/sys/group/` → `/api/v1/sys/group/`

业务逻辑不变，仅语义替换。
