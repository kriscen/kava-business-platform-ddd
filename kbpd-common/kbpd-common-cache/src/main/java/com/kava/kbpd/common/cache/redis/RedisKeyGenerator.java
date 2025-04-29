package com.kava.kbpd.common.cache.redis;

import ch.qos.logback.core.CoreConstants;
import com.kava.kbpd.common.core.constants.CoreConstant;

/**
 * @author Kris
 * @date 2025/4/1
 * @description: redis 生成类
 * <p>
 * 格式 ：{env}:{tenant}:{业务模块}:{对象类型}:{ID}[:字段]
 * <p>
 * 示例 ： dev:tenant123:user:account:1001（用户基础信息）
 */
public class RedisKeyGenerator {

    /**
     * 环境
     */
    private final String activeProfile;

    public RedisKeyGenerator(String activeProfile) {
        this.activeProfile = activeProfile;
    }

    /**
     * 生成redis Key
     * @param module 模块
     * @param keyType 缓存key类型
     * @param id 缓存id
     * @return redis key
     */
    public String generateKey(RedisKeyModule module, String keyType, String id) {
        return activeProfile + CoreConstant.SEPARATOR + module.getModule() + CoreConstant.SEPARATOR +
                keyType + CoreConstant.SEPARATOR + id;
    }

    /**
     * 生成带字段的 Key（适用于 Hash 类型）
     * @param module 模块
     * @param keyType 缓存key类型
     * @param id 缓存id
     * @param field hash 字段
     * @return redis key
     */
    public String generateKey(RedisKeyModule module, String keyType, String id, String field) {
        return generateKey(module, keyType, id) + CoreConstant.SEPARATOR + field;
    }

}