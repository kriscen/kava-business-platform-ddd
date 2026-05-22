## MODIFIED Requirements

### Requirement: DomainService 保留接口扩展点
MUST 创建 ISysI18nMessageService 接口及其实现类。实现类采用委托型模式，注入 ISysI18nMessageRepository 并将所有方法转发至 Repository。AppService 通过 Domain Service 调用 Repository。

#### Scenario: DomainService 接口和实现类存在
- **WHEN** 查看领域服务层
- **THEN** MUST 存在 ISysI18nMessageService 接口
- **AND** MUST 存在对应的实现类 SysI18nMessageService
- **AND** 实现类 MUST 注入 ISysI18nMessageRepository
- **AND** 实现类的 5 个方法（create、update、queryPage、queryById、removeBatchByIds）MUST 委托给 Repository

#### Scenario: AppService 通过 DomainService 调用
- **WHEN** SysI18nAppService 执行任何数据操作
- **THEN** MUST 通过 ISysI18nMessageService 调用
- **AND** MUST NOT 直接注入 ISysI18nMessageRepository
