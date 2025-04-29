package com.kava.kbpd.common.security.config;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.dao.auto.SaTokenDaoByObjectFollowString;
import com.kava.kbpd.common.cache.redis.RedisKeyGenerator;
import com.kava.kbpd.common.cache.redis.RedisKeyModule;
import com.kava.kbpd.common.cache.redis.RedissonService;
import com.kava.kbpd.common.security.enums.AuthRedisKeyType;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Kris
 * @date 2025/4/3
 * @description: saToken 缓存自己实现
 */
@Slf4j
@Component
public class AuthDaoByCache implements SaTokenDaoByObjectFollowString {

    @Resource
    private RedissonService redissonService;

    @Resource
    private RedisKeyGenerator redisKeyGenerator;

    private static final long DEFAULT_TIMEOUT = 10 * 60 * 1000;

    private static final long MILLISECOND = 1000;


    @Override
    public String get(String key) {
        return redissonService.getValue(getRedisKey(key));
    }

    @Override
    public void set(String key, String value, long timeout) {
        if(timeout == 0 || timeout <= SaTokenDao.NOT_VALUE_EXPIRE)  {
            return;
        }
        redissonService.setValue(getRedisKey(key), value, timeout * MILLISECOND);
    }

    @Override
    public void update(String key, String value) {
        redissonService.setValue(getRedisKey(key), value);
    }

    @Override
    public void delete(String key) {
        redissonService.remove(getRedisKey(key));
    }

    @Override
    public long getTimeout(String key) {
        return redissonService.getExpireTIme(getRedisKey(key));
    }

    @Override
    public void updateTimeout(String key, long timeout) {
        redissonService.expire(key, timeout * MILLISECOND);
    }

    @Override
    public List<String> searchData(String prefix, String keyword, int start, int size, boolean sortType) {
        //暂时不使用keys查询，因为keys查询会耗时
        return List.of();
    }

    private String getRedisKey(String key){
        return redisKeyGenerator.generateKey(RedisKeyModule.AUTH, AuthRedisKeyType.SA_TOKEN.getType(),key);
    }
}
