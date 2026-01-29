package com.kava.kbpd.auth.oauth2.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.kava.kbpd.auth.model.ExtendAuthenticationToken;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;

/**
 * @author Kris
 * @date 2025/4/19
 * @description: oauth2 module
 */
public class CustomerOauth2Module extends SimpleModule {
    public CustomerOauth2Module() {
        super(CustomerOauth2Module.class.getName());
        this.setMixInAnnotation(OAuth2Authorization.class, OAuth2AuthorizationMixin.class);
        this.setMixInAnnotation(AuthorizationGrantType.class, AuthorizationGrantTypeMixin.class);
        this.setMixInAnnotation(OAuth2AuthorizationConsent.class, OAuth2AuthorizationConsentMixin.class);
        this.setMixInAnnotation(OAuth2Authorization.Token.class, OAuth2AuthorizationTokenMixin.class);
        this.setMixInAnnotation(OAuth2AuthorizationCode.class, OAuth2AuthorizationCodeMixin.class);
        this.setMixInAnnotation(OAuth2AccessToken.class, OAuth2AccessTokenMixin.class);
        this.setMixInAnnotation(OAuth2AccessToken.TokenType.class, OAuth2AccessTokenTypeMixin.class);
        this.setMixInAnnotation(OAuth2RefreshToken.class, OAuth2RefreshTokenMixin.class);
        this.setMixInAnnotation(Long.class, Object.class);
        this.setMixInAnnotation(ExtendAuthenticationToken.class,ExtendAuthenticationTokenMixin.class);
    }
}
