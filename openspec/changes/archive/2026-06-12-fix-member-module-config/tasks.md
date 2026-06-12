## 1. parent pom 补充 build 配置

- [x] 1.1 在 `kbpd-member/pom.xml` 添加 `<build>` 段，包含 `maven-compiler-plugin`、`maven-resources-plugin`、`versions-maven-plugin`，与 UPMS parent pom 保持一致

## 2. types 层依赖修正

- [x] 2.1 修改 `kbpd-member-types/pom.xml`：移除 `spring-boot-starter-web` 和 `commons-lang3`，添加 `kbpd-common-core`

## 3. domain 层依赖修正

- [x] 3.1 修改 `kbpd-member-domain/pom.xml`：添加 `spring-context`，移除冗余的直接 `kbpd-common-core` 依赖（已通过 types 传递）

## 4. application 层依赖补充 + 清理

- [x] 4.1 修改 `kbpd-member-application/pom.xml`：添加 `mapstruct`、`mapstruct-processor`、`lombok-mapstruct-binding`、`spring-tx`、`slf4j-api`；将硬编码的 `maven.compiler.source/target` 改为 `${java.version}`
- [x] 4.2 删除 `kbpd-member-application/src/main/java/com/kava/kbpd/App.java`

## 5. api 层依赖补充

- [x] 5.1 修改 `kbpd-member-api/pom.xml`：添加 `jackson-annotations`

## 6. adapter 层依赖替换

- [x] 6.1 修改 `kbpd-member-adapter/pom.xml`：将 `spring-boot-starter-web` 替换为 `kbpd-common-web`，添加 `nacos-discovery` 和 `nacos-config`
- [x] 6.2 将 `kbpd-member-adapter/pom.xml` 中的 `<finalName>kbpd-member-trigger</finalName>` 修正为 `<finalName>kbpd-member-adapter</finalName>`

## 7. bootstrap 层安全激活 + 测试

- [x] 7.1 在 `MemberApplication.java` 添加 `@EnableResourceServer` 注解
- [x] 7.2 在 `kbpd-member-bootstrap/pom.xml` 添加 `junit` 测试依赖
- [x] 7.3 创建 `kbpd-member-bootstrap/src/test/java/com/kava/kbpd/member/MemberApplicationTest.java` 冒烟测试

## 8. 验证

- [x] 8.1 执行 `mvn clean compile -pl kbpd-member/kbpd-member-bootstrap -am -DskipTests`，确认构建成功且无新增 WARNING
- [x] 8.2 检查依赖树 `mvn dependency:tree -pl kbpd-member/kbpd-member-types`，确认无 `spring-boot-starter-web`
