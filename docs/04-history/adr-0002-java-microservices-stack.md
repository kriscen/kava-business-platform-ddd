# 2024-01-20 技术栈决策：Java 21 + Spring Cloud Alibaba

## 状态
已采纳

## 背景

项目需要选择主要技术栈。技术选型需要考虑：
- 团队技术储备
- 生态系统成熟度
- 长期维护成本
- 性能与稳定性

## 决策

| 类别 | 技术 | 版本 |
|------|------|------|
| 运行时 | Java | 21 |
| 框架 | Spring Boot | 3.3.x |
| 微服务 | Spring Cloud | 2023.x |
| 云生态 | Spring Cloud Alibaba | 2023.x |
| RPC | Apache Dubbo | 3.3.x |
| 数据库 | MySQL + MyBatis-Plus | 8.x / 3.5.x |
| 缓存 | Redis | - |
| 注册/配置 | Nacos | - |
| 安全 | Spring Security + OAuth2 | - |

**不走 JDK 17 而直接上 Java 21**：Java 21 是 LTS 版本，包含虚拟线程（Virtual Threads）、模式匹配等生产级特性，提前布局现代化 Java。

## 理由

1. **Spring Boot 3.x**：要求 Java 17+，充分利用新版本语言特性
2. **Spring Cloud Alibaba**：国内生态成熟，Nacos、Sentinel 与业务场景高度契合
3. **Dubbo 3.x**：相比 Spring Cloud Feign，在复杂微服务场景下性能更优，支持多协议
4. **MyBatis-Plus**：国产生态增强，CRUD 开发效率高，与现有团队技能匹配

## 替代方案

| 方案 | 优点 | 缺点 |
|------|------|------|
| Go + k8s | 轻量、高并发 | 团队 Go 经验少，生态不如 Java 成熟 |
| .NET Core | 开发效率高 | 国产云厂商支持弱 |
| Python | 快速原型 | 性能弱，难以支撑企业级复杂业务 |

## 后果

- **正面**：Java 21 虚拟线程可大幅提升并发处理能力
- **正面**：Spring Cloud Alibaba 组件开箱即用，减少自研成本
- **约束**：统一开发环境为 JDK 21，避免版本碎片化
- **约束**：禁止在 Domain 层直接使用 Spring 注解（如 @Service、@Repository）
