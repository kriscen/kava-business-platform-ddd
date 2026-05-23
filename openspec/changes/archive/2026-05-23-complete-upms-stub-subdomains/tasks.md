## 1. Menu 子域 DomainService 补齐

- [x] 1.1 ISysMenuService 接口新增 `queryAll()` 和 `queryByIds(List<SysMenuId>)` 方法签名
- [x] 1.2 SysMenuService 实现类：注入 ISysMenuRepository，实现全部 CRUD 方法委托转发 + queryAll + queryByIds

## 2. Dept 子域 DomainService 补齐

- [x] 2.1 SysDeptService 实现类：注入 ISysDeptRepository，实现全部 CRUD 方法委托转发

## 3. OauthClient 子域 DomainService 补齐

- [x] 3.1 ISysOauthClientService 接口新增 `queryByClientId(String)` 方法签名
- [x] 3.2 SysOauthClientService 实现类：注入 ISysOauthClientRepository，实现全部 CRUD 方法委托转发 + queryByClientId

## 4. 验证

- [x] 4.1 执行 `mvn clean install -pl kbpd-upms -am -DskipTests` 确认编译通过
