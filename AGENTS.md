# AGENTS.md

This file provides guidance to Codex (Codex.ai/code) when working with code in this repository.

## Build Commands

```bash
# Build entire project
mvn clean install

# Build a specific module (e.g., kbpd-upms)
mvn clean install -pl kbpd-upms -am

# Build without running tests
mvn clean install -DskipTests

# Run tests for a specific module
mvn test -pl kbpd-upms/kbpd-upms-bootstrap

# Run a single test class
mvn test -pl kbpd-upms/kbpd-upms-bootstrap -Dtest=UpmsApplicationTest
```

## Running Services

Services are Spring Boot applications. Run from the bootstrap module:

```bash
# Run UPMS service
cd kbpd-upms/kbpd-upms-bootstrap && mvn spring-boot:run

# Run Auth service
cd kbpd-auth && mvn spring-boot:run

# Run Gateway
cd kbpd-gateway && mvn spring-boot:run
```

**Note**: Services require Nacos for service discovery and configuration. Ensure Nacos is running before starting services.

## Architecture

This project implements Domain-Driven Design (DDD) with Hexagonal Architecture (Ports and Adapters).

### Dependency Direction

Dependencies must flow inward: `adapter/infrastructure → application → domain`

- **domain**: Core business logic - must NOT depend on Spring Boot / MyBatis etc., allows lightweight spring-context annotations
- **application**: Application services that orchestrate domain objects
- **adapter**: Controllers, RPC implementations - converts external requests to domain format
- **infrastructure**: Persistence implementations, external service access
- **api**: DTOs, query objects, request/response models, service interfaces (port definitions)
- **types**: Enums, constants, common type definitions
- **bootstrap**: Spring Boot application entry point, wires all layers together

### Domain Layer Details

Domain entities use value object IDs (e.g., `SysUserId`, `SysTenantId`) from `kbpd-common-core`. Aggregates implement `AggregateRoot<ID>` interface. Repositories are split into read/write interfaces (`ISysUserReadRepository`, `ISysUserWriteRepository`) following the CQRS pattern.

### Infrastructure Layer Details

- `dao/`: MyBatis-Plus mappers
- `dao/po/`: Persistence objects (database row mapping)
- `adapter/repository/`: Repository interface implementations
- `converter/`: PO ↔ Domain entity converters (MapStruct)

### Module Structure

```
kbpd-{business}/
├── api/           # Port definitions (DTOs, interfaces)
├── application/   # Application services
├── domain/        # Core domain (entities, aggregates, domain services, repository interfaces)
├── infrastructure/# Persistence and external service implementations
├── adapter/       # Controllers, RPC adapters
├── types/         # Enums, constants
└── bootstrap/     # Spring Boot entry point
```

### Key Modules

- **kbpd-upms**: User permission management service (full DDD structure)
- **kbpd-member**: Member service (full DDD structure)
- **kbpd-auth**: OAuth2 authorization server (standalone Spring Boot app, not DDD-structured)
- **kbpd-gateway**: API gateway (Spring Cloud Gateway, standalone)
- **kbpd-common**: Shared utilities
  - `kbpd-common-bom`: Dependency version management
  - `kbpd-common-core`: Base classes, value object IDs, enums, error codes
  - `kbpd-common-database`: MyBatis-Plus and Druid configuration
  - `kbpd-common-security`: Spring Security utilities
  - `kbpd-common-cache`: Redis configuration
  - `kbpd-common-web`: Web-layer utilities
- **kbpd-demo**: Example service (commented out in root pom, not available)

## Technology Stack

- **Java 21** with **Spring Boot 3.3.x**, **Spring Cloud 2023.x**, **Spring Cloud Alibaba 2023.x**
- **Apache Dubbo 3.3.x** for RPC between services
- **Nacos** for service discovery and configuration
- **MyBatis-Plus 3.5.x** for data access
- **Druid** for database connection pooling
- **Redis** for caching
- **Spring Security + OAuth2** for authentication
- **MapStruct** for object mapping
- **Lombok** for reducing boilerplate

## DDD Implementation Rules

Detailed rules: [`docs/03-conventions/ddd-rules.md`](docs/03-conventions/ddd-rules.md)

1. **Domain model**: 充血方向，逐步完善。有业务规则时将行为收入 Entity/AggregateRoot
2. **Spring in Domain**: 允许 `@Service` + 构造器注入，禁止 `@Resource`/`@Autowired` 字段注入。pom 依赖 `spring-context`（仅注解），不依赖 `spring-boot-starter`
3. **DomainService**: 按需创建（跨 Repo 编排、业务校验、跨聚合协调、非平凡算法时才建）。保留接口作扩展点，未使用的实现不注入 Repo
4. **跨聚合访问**: DomainService 允许跨聚合读，禁止跨聚合写。跨聚合写在 AppService 的 `@Transactional` 中编排
5. **层间引用**: ID 和 ListQuery 留在 domain。Adapter 可引用 `domain.model.valobj`（ID、ListQuery），禁止引用 entity/aggregate/service/repository
6. **ID 提升规则**: 模块内部 ID 留在 domain，跨模块共享时提升到 common-core（按需提升，不预设）
7. **Critical path testing**: Tests required for operations involving funds, permissions, or data integrity

<!-- KAVA-PROJECT-RULES: do NOT remove next line on /init — keeps project-specific docs/openspec rules -->
@.Codex/rules/project-docs.md
<!-- KAVA-PROJECT-RULES: do NOT remove next line on /init — keeps product vision rules -->
@.Codex/rules/product-vision.md

## Project Workflow Skills (OpenSpec)

The project uses OpenSpec for structured change management. Artifacts are stored in `openspec/` directory with subdirectories for each change.

Codex project skills live in `.agents/skills/`. Use them by naming the skill or by asking naturally for the corresponding workflow:

- `openspec-propose` - Propose a new change with all artifacts (proposal, specs, design, tasks). Claude alias: `/opsx:propose`
- `openspec-new-change` - Start a new change using the OpenSpec workflow. Claude alias: `/opsx:new`
- `openspec-apply-change` - Implement tasks from an OpenSpec change. Claude alias: `/opsx:apply`
- `openspec-verify-change` - Verify implementation matches change artifacts. Claude alias: `/opsx:verify`
- `openspec-archive-change` - Archive a completed change. Claude alias: `/opsx:archive`
- `openspec-sync-specs` - Sync delta specs from a change to main specs. Claude alias: `/opsx:sync`
- `openspec-explore` - Explore ideas and investigate problems. Claude alias: `/openspec-explore`
- `openspec-cleanup` - 整理 openspec 目录：清理归档冗余 delta specs、合并相关 main specs. Claude alias: `/openspec-cleanup`

OpenSpec artifact rules (from `openspec/config.yaml`):
- Use Chinese language for all artifacts
- Proposal: Explain Why and What, list affected files
- Specs: Requirement/Scenario format with WHEN/THEN/AND structure
- Design: Include Goals/Non-Goals, decisions with Because... reasoning
- Tasks: Each task under 2 hours, last task is verification

## Utility Skills

- `update-docs-map` - 扫描 docs/ 目录，自动更新 `docs/00-project-map.md` 文档地图索引。Claude alias: `/update-docs-map`

## Codex Migration Notes

- Codex canonical entry point: `AGENTS.md`
- Codex project rules: `.Codex/rules/`
- Codex project skills: `.agents/skills/`
- Legacy Claude files under `.claude/` and `CLAUDE.md` are retained for history only. New project instructions should be added to `AGENTS.md`, `.Codex/rules/`, or `.agents/skills/`.
- Claude local permission settings are not migrated into the repository. Codex permissions are controlled by the current Codex client/session approval system.
