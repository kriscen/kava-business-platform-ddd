package com.kava.kbpd.auth.test;

import com.kava.kbpd.upms.api.service.IRemoteOauthClientDetailService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;

/**
 * @author Kris
 * @date 2025/4/8
 * @description:
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthApplicationTest {
    @Resource
    private RedisTemplate<String,OAuth2Authorization> redisTemplate;

    @DubboReference(version = "1.0")
    private IRemoteOauthClientDetailService remoteOauthClientDetailService;

    @Test
    public void test2() throws Exception {
        System.out.println(remoteOauthClientDetailService.findByClientId("123"));
    }

    @Test
    public void test1() throws Exception {
        OAuth2Authorization consent =
                OAuth2Authorization.withRegisteredClient(RegisteredClient.withId("123")
                                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                                .clientId("123")
                        .redirectUri("46546").build())
                        .token(new OAuth2AuthorizationCode("123", Instant.now(),Instant.MAX))
                        .principalName("4568")
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .build();

        redisTemplate.opsForValue().set("2222",consent);
        OAuth2Authorization o = redisTemplate.opsForValue().get("2222");
        System.out.println(o);

    }

}
