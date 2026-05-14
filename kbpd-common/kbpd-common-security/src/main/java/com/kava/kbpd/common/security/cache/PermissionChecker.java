package com.kava.kbpd.common.security.cache;

import com.kava.kbpd.common.core.model.UserContext;
import com.kava.kbpd.common.security.context.UserContextHolder;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 权限校验组件：从缓存校验当前用户权限
 */
@Component("permissionChecker")
public class PermissionChecker {

    @Resource
    private PermissionCacheService permissionCacheService;

    /**
     * 校验当前用户是否拥有指定权限
     * 超级管理员角色（ROLE_ADMIN）跳过所有权限检查
     */
    public boolean hasPermission(String permission) {
        UserContext ctx = UserContextHolder.get();
        if (ctx == null || ctx.getUserId() == null) {
            return false;
        }
        // 超级管理员跳过权限检查
        if (isAdmin(ctx.getRoles())) {
            return true;
        }
        Set<String> permissions = permissionCacheService.getPermissions(ctx.getUserId());
        if (permissions == null || permissions.isEmpty()) {
            return false;
        }
        return permissions.contains(permission);
    }

    private boolean isAdmin(Set<String> roles) {
        return roles != null && roles.contains("ROLE_ADMIN");
    }
}
