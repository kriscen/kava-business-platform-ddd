# 2024-02-05 采用 CQRS 模式：Repository 读写分离

## 状态
已采纳

## 背景

在 DDD 实践中，发现传统的单一 Repository 模式存在以下问题：

- **读写耦合**：同一个接口既要处理复杂业务校验（写），又要提供高效查询（读）
- **性能矛盾**：写操作需要事务锁，读操作需要优化索引，两者需求冲突
- **职责模糊**：Service 层难以区分是编排领域逻辑还是处理查询

## 决策

将 Repository 拆分为**读接口**和**写接口**两套：

```java
// 写接口 - 负责领域逻辑执行
public interface ISysUserWriteRepository {
    void save(SysUserAggregate user);
    void update(SysUserAggregate user);
    void delete(SysUserId id);
}

// 读接口 - 负责高效查询
public interface ISysUserReadRepository {
    SysUserDTO findById(SysUserId id);
    Page<SysUserDTO> findPage(UserPageQuery query);
    List<SysUserDTO> findByRoleId(RoleId roleId);
}
```

应用层也对应拆分：

```
application/
├── command/        # 命令处理（CUD 操作）
│   └── sysuser/
│       └── SysUserCommandHandler.java
└── query/          # 查询处理（读操作）
    └── sysuser/
        └── SysUserQueryHandler.java
```

## 理由

1. **职责单一**：写接口专注业务规则执行，读接口专注数据查询
2. **性能优化**：读接口可直接映射 DTO，避免领域对象转换开销
3. **独立演进**：读写可以独立优化，不互相影响
4. **CQRS 清晰**：命令（Command）与查询（Query）分离符合 CQRS 原则

## 替代方案

| 方案 | 优点 | 缺点 |
|------|------|------|
| 单一日志 Repository | 简单 | 读写耦合 |
| CQRS + Event Sourcing | 最彻底 | 复杂度高 |

## 后果

- **正面**：读写分离，代码职责清晰
- **正面**：查询性能优化空间更大（可独立优化读库）
- **约束**：写操作必须通过聚合根完成，禁止绕过聚合根直接持久化
- **约束**：读接口直接返回 DTO，不暴露领域对象给适配器层
