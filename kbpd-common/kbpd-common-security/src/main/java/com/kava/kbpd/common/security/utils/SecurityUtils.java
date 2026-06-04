package com.kava.kbpd.common.security.utils;

import com.kava.kbpd.common.core.model.UserContext;
import com.kava.kbpd.common.security.context.UserContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Kris
 * @date 2025/4/14
 * @description: Spring Security 工具类
 */
public class SecurityUtils {

    /**
     * 获取统一用户上下文
     */
    public static UserContext getUserContext() {
        return UserContextHolder.get();
    }

    /**
     * 获取租户ID
     */
    public static Long getTenantId() {
        UserContext ctx = getUserContext();
        return ctx != null ? ctx.getTenantId() : null;
    }

    /**
     * 获取用户类型
     */
    public static String getUserType() {
        UserContext ctx = getUserContext();
        return ctx != null ? ctx.getUserType() : null;
    }

    public static Long getUserId() {
        UserContext ctx = getUserContext();
        return ctx != null ? ctx.getUserId() : null;
    }

    public static String getUsername() {
        UserContext ctx = getUserContext();
        return ctx != null ? ctx.getUsername() : null;
    }

    public static Map<String, Object> getTokenAttributes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            return jwtAuthenticationToken.getTokenAttributes();
        }
        return null;
    }


    /**
     * 获取用户角色集合
     */
    public static Set<String> getRoles() {
        UserContext ctx = getUserContext();
        if (ctx != null && ctx.getRoles() != null) {
            return ctx.getRoles();
        }
        // fallback: 从 Authentication authorities 提取
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return AuthorityUtils.authorityListToSet(authentication.getAuthorities())
                    .stream()
                    .collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));
        }
        return null;
    }

    /**
     * 获取分组ID
     */
    public static Long getGroupId() {
        UserContext ctx = getUserContext();
        return ctx != null ? ctx.getGroupId() : null;
    }

    /**
     * 获取会员ID
     */
    public static Long getMemberId() {
        UserContext ctx = getUserContext();
        return ctx != null ? ctx.getMemberId() : null;
    }
}
