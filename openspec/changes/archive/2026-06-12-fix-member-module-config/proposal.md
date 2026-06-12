## Why

Member 模块首次搭建时，pom.xml 依赖配置与 UPMS 参考模块存在多处偏差，导致类型层过重、应用层缺少必要依赖、安全认证未激活等问题。这些问题不修复，后续开发会频繁遇到编译失败和运行时认证绕过。

## What Changes

- **types 层**：移除 `spring-boot-starter-web`，改依赖 `kbpd-common-core`，保持轻量
- **domain 层**：添加 `spring-context`（支持 `@Service` + 构造器注入），移除冗余的直接 `kbpd-common-core` 依赖
- **application 层**：添加 `mapstruct`、`spring-tx`、`slf4j-api`，删除 archetype 残留 `App.java`
- **api 层**：添加 `jackson-annotations`
- **adapter 层**：将 `spring-boot-starter-web` 替换为 `kbpd-common-web`，添加 `nacos-discovery` 和 `nacos-config`，修正 `finalName`
- **bootstrap 层**：在 `MemberApplication` 添加 `@EnableResourceServer`，补充 junit 测试依赖，添加上下文加载冒烟测试
- **parent pom**：补充 `<build>` 段统一编译配置

## Capabilities

### New Capabilities

（无新增能力，本次为配置修复）

### Modified Capabilities

（无需求变更，本次仅为依赖和配置修正）

## Impact

受影响文件全部在 `kbpd-member` 模块内：

- `kbpd-member/pom.xml` — 补充 build 配置
- `kbpd-member/kbpd-member-types/pom.xml` — 依赖修正
- `kbpd-member/kbpd-member-domain/pom.xml` — 依赖修正
- `kbpd-member/kbpd-member-application/pom.xml` — 依赖补充
- `kbpd-member/kbpd-member-api/pom.xml` — 依赖补充
- `kbpd-member/kbpd-member-adapter/pom.xml` — 依赖替换 + finalName 修正
- `kbpd-member/kbpd-member-bootstrap/pom.xml` — 测试依赖补充
- `kbpd-member/kbpd-member-bootstrap/src/main/java/.../MemberApplication.java` — 添加安全注解
- `kbpd-member/kbpd-member-application/src/main/java/com/kava/kbpd/App.java` — 删除
- `kbpd-member/kbpd-member-bootstrap/src/test/java/.../MemberApplicationTest.java` — 新增冒烟测试
