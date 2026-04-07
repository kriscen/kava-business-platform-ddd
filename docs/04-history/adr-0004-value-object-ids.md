# 2024-02-10 值对象 ID：强类型标识符

## 状态
已采纳

## 背景

在 DDD 实践中，传统使用 `Long` 或 `String` 作为实体 ID 存在类型安全问题：

```java
// 问题：两个 Long 参数顺序错误，编译器无法检测
void assignRole(Long userId, Long roleId) {
    // 业务逻辑
}

// 问题：跨上下文传递 ID，容易混淆
SysUserDTO getUserById(Long userId);  // 是哪个上下文的 userId？
```

## 决策

为每个聚合根定义**强类型标识符（Value Object）**：

```java
// 用户聚合根标识符
public final class SysUserId extends SingleValueObject<Long> {
    public static SysUserId of(Long value) { ... }
}

// 租户聚合根标识符
public final class SysTenantId extends SingleValueObject<Long> {
    public static SysTenantId of(Long value) { ... }
}

// 角色聚合根标识符
public final class SysRoleId extends SingleValueObject<Long> {
    public static SysRoleId of(Long value) { ... }
}
```

所有领域对象的方法签名使用强类型 ID：

```java
// 明确参数类型和含义
public void assignRole(SysUserId userId, SysRoleId roleId) { ... }

// 跨上下文接口也使用强类型
public interface SysUserFacadeService {
    SysUserDTO getUserById(SysUserId userId);  // 类型明确
}
```

## 理由

1. **类型安全**：编译期检查，防止 ID 类型混淆（如 userId 和 roleId 互换）
2. **表达业务**：ID 本身是有业务含义的概念，不是无意义的数字
3. **重构安全**：修改 ID 内部表示不影响使用方
4. **DDD 纯粹性**：符合"值对象表达概念"的原则

## 替代方案

| 方案 | 优点 | 缺点 |
|------|------|------|
| Long/UUID | 简单 | 类型不安全 |
| String (UUID) | 全局唯一 | 无语义，序列化开销大 |
| 只在 Domain 用 VO，API 用 Long | 折中 | 不够彻底 |

## 后果

- **正面**：类型安全，编译期可发现错误
- **正面**：代码自文档化，方法签名表达意图
- **约束**：Domain 层必须使用强类型 ID，不得使用原始类型
- **约束**：API 层可通过 DTO 转换为 Long/String 以保持兼容性
