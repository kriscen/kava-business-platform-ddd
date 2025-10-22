package com.kava.kbpd.auth.oauth2.component;

import com.kava.kbpd.auth.config.KbpdAuthProperties;
import com.kava.kbpd.auth.enums.AuthRedisKeyType;
import com.kava.kbpd.common.cache.redis.IRedisService;
import com.kava.kbpd.common.cache.redis.RedisKeyGenerator;
import com.kava.kbpd.common.cache.redis.RedisKeyModule;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author Kris
 * @date 2025/4/1
 * @description: RedisAuthorizationConsentService
 * <p>
 * 将consent使用redis 存储
 */
@Slf4j
@Component
public class RedisAuthorizationConsentService implements OAuth2AuthorizationConsentService {

    @Resource
    private IRedisService redisService;
    @Resource
    private RedisKeyGenerator redisKeyGenerator;

    @Resource
    private KbpdAuthProperties kbpdAuthProperties;

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        redisService.setValue(getRedisKey(authorizationConsent.getRegisteredClientId(),authorizationConsent.getPrincipalName()),authorizationConsent,kbpdAuthProperties.getAuthorizationConsentTimeout());
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        redisService.remove(getRedisKey(authorizationConsent.getRegisteredClientId(),authorizationConsent.getPrincipalName()));
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        return redisService.getValue(getRedisKey(registeredClientId, principalName));
    }

    private String getRedisKey(String registeredClientId, String principalName) {
        return redisKeyGenerator.generateKey(RedisKeyModule.AUTH, AuthRedisKeyType.CONSENT.getType(),
                principalName,registeredClientId);
    }

}
