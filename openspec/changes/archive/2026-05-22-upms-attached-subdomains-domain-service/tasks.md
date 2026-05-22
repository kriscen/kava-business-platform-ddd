## 1. Domain Service 实现（kbpd-upms-domain）

- [x] 1.1 SysPublicParamService — 注入 ISysPublicParamRepository，5 个方法委托转发
- [x] 1.2 SysLogService — 注入 ISysLogRepository，5 个方法委托转发
- [x] 1.3 SysFileGroupService — 注入 ISysFileGroupRepository，5 个方法委托转发
- [x] 1.4 SysFileService — 注入 ISysFileRepository，5 个方法委托转发
- [x] 1.5 SysRouteConfService — 注入 ISysRouteConfRepository，5 个方法委托转发
- [x] 1.6 SysAuditLogService — 注入 ISysAuditLogRepository，5 个方法委托转发
- [x] 1.7 新建 SysI18nMessageService — 注入 ISysI18nMessageRepository，5 个方法委托转发

## 2. App Service 调用链修正（kbpd-upms-application）

- [x] 2.1 SysPublicParamAppService — Repository 注入改为 DomainService 注入，方法调用改为 domainService
- [x] 2.2 SysLogAppService — 同上
- [x] 2.3 SysFileGroupAppService — 同上
- [x] 2.4 SysFileAppService — 同上
- [x] 2.5 SysRouteConfAppService — 同上
- [x] 2.6 SysAuditLogAppService — 同上
- [x] 2.7 SysI18nAppService — 同上

## 3. 基础设施修复（kbpd-upms-infrastructure）

- [x] 3.1 SysAuditLogRepository.queryPage — 取消注释并补全查询条件构建逻辑，参照 SysLogRepository 等实现

## 4. 验证

- [x] 4.1 执行 `mvn clean install -pl kbpd-upms -am` 确保编译通过
- [x] 4.2 检查 7 个 Domain Service 实现类均存在且无 UnsupportedOperationException
- [x] 4.3 检查 7 个 AppService 均不再直接注入 Repository
