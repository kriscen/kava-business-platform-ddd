package com.kava.kbpd.common.core.model;

import cn.hutool.core.convert.Convert;
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
     * 部门ID（仅 B端用户有值）
     */
    private Long deptId;

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
                .tenantId(Convert.toLong(claims.get(JwtClaimConstants.TENANT_ID)))
                .userType(Convert.toStr(claims.get(JwtClaimConstants.USER_TYPE)))
                .userId(Convert.toLong(claims.get(JwtClaimConstants.USER_ID)))
                .memberId(Convert.toLong(claims.get(JwtClaimConstants.MEMBER_ID)))
                .username(Convert.toStr(claims.get(JwtClaimConstants.USERNAME)))
                .deptId(Convert.toLong(claims.get(JwtClaimConstants.DEPT_ID)))
                .roles(roles)
                .dataScope(Convert.toStr(claims.get(JwtClaimConstants.DATA_SCOPE)))
                .build();
    }
}
