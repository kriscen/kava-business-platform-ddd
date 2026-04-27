# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

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

- **domain**: Core business logic - must NOT depend on any external frameworks (Spring, database drivers)
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
- **kbpd-common**: Shared utilities (core, database, security, cache, web, bom)
- **kbpd-demo**: Example service (commented out in root pom, not available)

## Technology Stack

- **Java 21** with **Spring Boot 3.3.x**, **Spring Cloud 2023.x**, **Spring Cloud Alibaba 2023.x**
- **Apache Dubbo 3.3.x** for RPC between services
- **Nacos** for service discovery and configuration
- **MyBatis-Plus 3.5.x** for data access
- **Spring Security + OAuth2** for authentication
- **MapStruct** for object mapping
- **Lombok** for reducing boilerplate

## DDD Constraints (from constitution.md)

1. **Domain layer isolation**: Domain must not depend on Spring or external frameworks
2. **Business logic location**: All business rules must be in domain layer, not leaked to application or adapter
3. **Critical path testing**: Tests required for operations involving funds, permissions, or data integrity
4. **Dependency inward**: adapter/infrastructure → application → domain

## Project Workflow Skills (OpenSpec)

The project uses OpenSpec for structured change management. Artifacts are stored in `openspec/` directory with subdirectories for each change.

Available skills:
- `/opsx:propose` - Propose a new change with all artifacts (proposal, specs, design, tasks)
- `/opsx:new` - Start a new change using the OpenSpec workflow
- `/opsx:continue` - Create the next artifact for an in-progress change
- `/opsx:ff` - Fast-forward through artifact creation
- `/opsx:verify` - Verify implementation matches change artifacts
- `/opsx:archive` - Archive a completed change
- `/opsx:sync` - Sync delta specs from a change to main specs
- `/opsx:onboard` - Walk through a complete OpenSpec workflow cycle
- `/openspec-explore` - Explore ideas and investigate problems

OpenSpec artifact rules (from `openspec/config.yaml`):
- Use Chinese language for all artifacts
- Proposal: Explain Why and What, list affected files
- Specs: Requirement/Scenario format with WHEN/THEN/AND structure
- Design: Include Goals/Non-Goals, decisions with Because... reasoning
- Tasks: Each task under 2 hours, last task is verification

## Utility Skills

- `/update-docs-map` - 扫描 docs/ 目录，自动更新 `docs/00-project-map.md` 文档地图索引