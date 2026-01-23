package com.kava.kbpd.auth.oauth2.component;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.kava.kbpd.auth.config.KbpdAuthProperties;
import com.kava.kbpd.auth.constants.AuthConstants;
import com.kava.kbpd.common.core.constants.SecretConstants;
import com.kava.kbpd.upms.api.model.dto.SysOauthClientDTO;
import com.kava.kbpd.upms.api.service.IRemoteOauthClientService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author Kris
 * @date 2025/4/1
 * @description: 从数据库查询客户端注册信息
 */
@Slf4j
@Component
public class DBRegisteredClientRepository implements RegisteredClientRepository {

    @DubboReference(version = "1.0")
    private IRemoteOauthClientService remoteOauthClientDetailService;

    @Resource
    private KbpdAuthProperties kbpdAuthProperties;

    @Override
    public void save(RegisteredClient registeredClient) {

    }

    @Override
    public RegisteredClient findById(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        SysOauthClientDTO clientDetails = remoteOauthClientDetailService.queryByClientId(clientId);

        RegisteredClient.Builder builder = RegisteredClient.withId(clientDetails.getClientId())
                .clientId(clientDetails.getClientId())
                .clientSecret(SecretConstants.NOOP + clientDetails.getClientSecret())
                .clientAuthenticationMethods(clientAuthenticationMethods -> {
                    clientAuthenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
                    clientAuthenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_POST);
                });

        for (String authorizedGrantType : clientDetails.getAuthorizedGrantTypes()) {
            builder.authorizationGrantType(new AuthorizationGrantType(authorizedGrantType));

        }
        // 回调地址
        Optional.ofNullable(clientDetails.getWebServerRedirectUri())
                .ifPresent(redirectUri -> Arrays.stream(redirectUri.split(StrUtil.COMMA))
                        .filter(StrUtil::isNotBlank)
                        .forEach(builder::redirectUri));

        // scope
        Optional.ofNullable(clientDetails.getScope())
                .ifPresent(scope -> Arrays.stream(scope.split(StrUtil.COMMA))
                        .filter(StrUtil::isNotBlank)
                        .forEach(builder::scope));

        return builder
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                        .accessTokenTimeToLive(Duration.ofSeconds(
                                Optional.ofNullable(clientDetails.getAccessTokenValidity()).orElse(kbpdAuthProperties.getAccessTokenValiditySeconds())))
                        .refreshTokenTimeToLive(Duration.ofSeconds(Optional.ofNullable(clientDetails.getRefreshTokenValidity())
                                .orElse(kbpdAuthProperties.getRefreshTokenValiditySeconds())))
                        .build())
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(!BooleanUtil.toBoolean(clientDetails.getAutoapprove()))
                        //传入userType和TenantId
                        .setting(AuthConstants.URL_PARAM_USER_TYPE,clientDetails.getUserType())
                        .setting(AuthConstants.URL_PARAM_TENANT_ID,clientDetails.getTenantId())
                        .build())
                .build();
    }
}
