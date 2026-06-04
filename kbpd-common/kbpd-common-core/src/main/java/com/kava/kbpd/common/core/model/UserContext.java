package com.kava.kbpd.common.core.model;

import com.kava.kbpd.common.core.constants.JwtClaimConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 统一用户上下文，封装从 JWT token 提取的完整用户信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserContext {

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 用户类型 ("1"=B端, "2"=C端)
     */
    private String userType;

    /**
     * B端用户ID（仅 userType="1" 时有值）
     */
    private Long userId;

    /**
     * C端会员ID（仅 userType="2" 时有值）
     */
    private Long memberId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 分组ID（仅 B端用户有值）
     */
    private Long groupId;

    /**
     * 角色集合
     */
    private Set<String> roles;

    /**
     * 数据权限范围（取用户主角色的 dsType）
     */
    private String dataScope;

    /**
     * 权限字符串集合
     */
    private Set<String> permissions;

    /**
     * 从 JWT claims 构造 UserContext
     */
    @SuppressWarnings("unchecked")
    public static UserContext fromJwtClaims(Map<String, Object> claims) {
        if (claims == null) {
            return null;
        }

        Set<String> roles = Collections.emptySet();
        Object rolesObj = claims.get(JwtClaimConstants.ROLES);
        if (rolesObj instanceof Set<?> set) {
            roles = set.stream().map(Object::toString).collect(Collectors.toSet());
        }

        return UserContext.builder()
                .tenantId(toLong(claims.get(JwtClaimConstants.TENANT_ID)))
                .userType(toStr(claims.get(JwtClaimConstants.USER_TYPE)))
                .userId(toLong(claims.get(JwtClaimConstants.USER_ID)))
                .memberId(toLong(claims.get(JwtClaimConstants.MEMBER_ID)))
                .username(toStr(claims.get(JwtClaimConstants.USERNAME)))
                .groupId(toLong(claims.get(JwtClaimConstants.GROUP_ID)))
                .roles(roles)
                .dataScope(toStr(claims.get(JwtClaimConstants.DATA_SCOPE)))
                .build();
    }

    private static Long toLong(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number n) {
            return n.longValue();
        }
        return Long.parseLong(value.toString());
    }

    private static String toStr(Object value) {
        return value == null ? null : value.toString();
    }
}
