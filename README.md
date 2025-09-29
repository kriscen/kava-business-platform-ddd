# Kava Business Platform DDD (KBPD)

## 项目简介

**Kava Business Platform DDD (KBPD)** 是一个基于领域驱动设计（DDD）和六边形架构思想构建的微服务项目实践。该项目旨在探索和应用现代软件架构原则，如分层解耦、领域建模、以及清晰的依赖关系管理，以构建可维护、可扩展的企业级应用。

**注意**: 该项目目前仍处于**前期开发/架构搭建阶段**，核心架构和基础模块已搭建完成，具体业务功能有待后续开发。

## 架构风格

*   **领域驱动设计 (DDD):** 采用 DDD 的核心概念，如实体（Entity）、聚合（Aggregate）、值对象（Value Object）、领域服务（Domain Service）、仓储（Repository）等，将业务逻辑清晰地封装在领域层。
*   **六边形架构 (Hexagonal Architecture) / 端口与适配器 (Ports and Adapters):** 通过 `api` (端口)、`domain` (核心)、`adapter` (适配器) 和 `infrastructure` (基础设施) 模块，实现核心业务逻辑与外部技术细节（如数据库、消息队列、HTTP API）的解耦。
*   **微服务 (Microservices):** 项目设计为多个独立部署的微服务，通过 Dubbo 进行服务间通信，通过 OAuth2 实现统一认证授权，并使用 Gateway 作为统一入口。

## 技术栈

*   **核心框架:** Spring Boot, Spring Cloud (隐含)
*   **安全框架:** Spring Security, OAuth2 (授权服务器与资源服务器)
*   **RPC 框架:** Apache Dubbo
*   **数据访问:** MyBatis-Plus
*   **数据库连接池:** Druid
*   **缓存:** Redis
*   **API 网关:** Spring Cloud Gateway
*   **服务发现/配置中心:** Nacos
*   **编程语言:** Java 21
*   **构建工具:** Maven
*   **辅助工具:** Lombok, MapStruct (用于对象映射), Hutool (通用工具库)

## 项目结构

项目采用 Maven 多模块结构，主要模块划分如下：

*   **根模块 (`kava-business-platform-ddd`):** 项目的聚合根，管理所有子模块。
*   **业务模块:**
  *   `kbpd-upms` (用户权限管理服务): 负责用户、角色、权限等基础管理功能。
  *   `kbpd-member` (会员服务): 负责会员相关业务逻辑。
  *   `kbpd-demo` (示例服务): 用于演示架构模式的示例模块。
*   **公共模块 (`kbpd-common`):**
  *   `kbpd-common-bom`: 统一管理项目依赖版本。
  *   `kbpd-common-core`: 提供核心基础类、枚举、错误码、通用模型等。
  *   `kbpd-common-database`: 数据库相关基础配置和工具。
  *   `kbpd-common-security`: 安全相关基础配置和工具。
  *   `kbpd-common-cache`: 缓存相关基础配置和工具。
*   **认证服务 (`kbpd-auth`):** 独立的 OAuth2 授权服务器。
*   **API 网关 (`kbpd-gateway`):** 统一的 API 入口，负责路由、认证、限流等。

每个业务模块内部，遵循 DDD 分层和六边形架构思想，进一步细分为：

*   `api`: 定义 DTO、查询对象、请求/响应对象及服务接口，作为外部交互的端口。
*   `application`: 应用服务层，协调领域对象完成业务流程。
*   `domain`: 领域模型层，包含核心业务逻辑和领域对象。
*   `infrastructure`: 基础设施层，实现持久化、外部服务访问等技术细节。
*   `adapter`: 适配器层，通常包含 Controller，将外部请求转换为领域层可处理的格式。
*   `types`: 通用类型定义，如枚举、常量等。


---

*   **作者:** Kris
