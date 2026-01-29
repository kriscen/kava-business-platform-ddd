package com.kava.kbpd.auth.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kava.kbpd.auth.model.ExtendAuthenticationToken;
import com.kava.kbpd.common.cache.redis.IRedisService;
import com.kava.kbpd.upms.api.service.IRemoteOauthClientService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.codec.JacksonCodec;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
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
    private IRedisService redisService;

    @DubboReference(version = "1.0")
    private IRemoteOauthClientService remoteOauthClientDetailService;

    @Test
    public void test2() throws Exception {
        System.out.println(remoteOauthClientDetailService.queryByClientId("123"));
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void test1() throws Exception {
//        OAuth2Authorization consent =
//                OAuth2Authorization.withRegisteredClient(RegisteredClient.withId("local")
//                                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                                .clientId("local")
//                        .redirectUri("46546").build())
//                        .token(new OAuth2AuthorizationCode("123", Instant.now(),Instant.MAX))
//                        .principalName("4568")
//                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                        .build();
//        redisService.setValue("2222",objectMapper.writeValueAsString(consent));
//        String o = redisService.getValue("2222");
//        System.out.println(o);
//        consent = objectMapper.readValue(o,OAuth2Authorization.class);
//        System.out.println(consent);
        ExtendAuthenticationToken t = new ExtendAuthenticationToken("1","2","3","4");

        redisService.setValue("333",t);
        t = redisService.getValue("333");
        System.out.println(t);

    }

}
