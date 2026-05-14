package com.kava.kbpd.common.security.cache;

import com.kava.kbpd.common.cache.redis.IRedisService;
import com.kava.kbpd.common.cache.redis.RedisKeyGenerator;
import com.kava.kbpd.common.cache.redis.RedisKeyModule;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 权限缓存服务：根据 userId 缓存 permissions 到 Redis
 */
@Component
public class PermissionCacheService {

    private static final String PERM_KEY_TYPE = "perm:user";
    private static final long DEFAULT_TTL_HOURS = 12;

    @Resource
    private IRedisService redisService;

    @Resource
    private RedisKeyGenerator redisKeyGenerator;

    /**
     * 获取用户权限集合，缓存未命中返回 null
     */
    @SuppressWarnings("unchecked")
    public Set<String> getPermissions(Long userId) {
        String key = buildKey(userId);
        Object val = redisService.getValue(key);
        if (val instanceof Set) {
            return (Set<String>) val;
        }
        return null;
    }

    /**
     * 缓存用户权限集合
     */
    public void cachePermissions(Long userId, Set<String> permissions) {
        String key = buildKey(userId);
        redisService.setValue(key, permissions, TimeUnit.HOURS.toSeconds(DEFAULT_TTL_HOURS));
    }

    /**
     * 清除用户权限缓存
     */
    public void evictPermissions(Long userId) {
        String key = buildKey(userId);
        redisService.remove(key);
    }

    private String buildKey(Long userId) {
        return redisKeyGenerator.generateKey(RedisKeyModule.UPMS, PERM_KEY_TYPE, String.valueOf(userId));
    }
}
