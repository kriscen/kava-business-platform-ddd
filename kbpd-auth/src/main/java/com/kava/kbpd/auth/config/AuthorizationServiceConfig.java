package com.kava.kbpd.auth.config;

import cn.hutool.core.util.StrUtil;
import com.kava.kbpd.auth.constants.AuthConstants;
import com.kava.kbpd.auth.model.MemberDetails;
import com.kava.kbpd.auth.model.SysUserDetails;
import com.kava.kbpd.auth.oauth2.component.*;
import com.kava.kbpd.common.cache.redis.IRedisService;
import com.kava.kbpd.common.cache.redis.RedisKeyGenerator;
import com.kava.kbpd.common.cache.redis.RedisKeyModule;
import com.kava.kbpd.common.core.constants.JwtClaimConstants;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Kris
 * @date 2025/4/10
 * @description: AuthorizationService 的配置
 */
@Configuration
public class AuthorizationServiceConfig {

    @Resource
    private IRedisService redisService;

    @Resource
    private RedisKeyGenerator redisKeyGenerator;

    @Resource
    private DBRegisteredClientRepository dbRegisteredClientRepository;

    @Resource
    private RedisAuthorizationService redisAuthorizationService;

    @Resource
    private RedisAuthorizationConsentService redisAuthorizationConsentService;

    @Resource
    private AuthenticationFailureEventHandler authenticationFailureEventHandler;

    @Resource
    private AuthenticationSuccessEventHandler authenticationSuccessEventHandler;

    /**
     * 一个用于 协议端点 的 Spring Security 过滤器链。
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
            throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                // RegisteredClientRepository （必需的） 用于管理新的和现有的客户。
                .registeredClientRepository(dbRegisteredClientRepository)
                //用于管理新的和现有的授权的 OAuth2AuthorizationService。
                .authorizationService(redisAuthorizationService)
                //OAuth2AuthorizationConsentService ，用于管理新的和现有的授权许可（Consent）。
                .authorizationConsentService(redisAuthorizationConsentService)
                //用于自定义OAuth2授权服务器的 configuration setting 的 AuthorizationServerSettings （必需的）。
                .authorizationServerSettings(authorizationServerSettings())
                //OAuth2TokenGenerator，用于生成OAuth2授权服务器支持的令牌。
                .tokenGenerator(tokenGenerator(jwkSource(), jwtTokenCustomizer()))
/*
                //OAuth2客户端认证的configurer。
//                .clientAuthentication(clientAuthentication ->
//                        clientAuthentication
//                                //添加一个 AuthenticationConverter（预处理器），当试图从 HttpServletRequest 中提取客户端凭证到 OAuth2ClientAuthenticationToken 的实例时使用。
//                                .authenticationConverter(authenticationConverter)
//                                //设置 Consumer，提供对默认和（可选）添加的 AuthenticationConverter List 的访问，允许添加、删除或定制特定的 AuthenticationConverter 的能力。
//                                .authenticationConverters(authenticationConvertersConsumer)
//                                //添加一个用于验证 OAuth2ClientAuthenticationToken 的 AuthenticationProvider（主处理器）。
//                                .authenticationProvider(authenticationProvider)
//                                //设置 Consumer，提供对默认和（可选）添加的 AuthenticationProvider List 的访问，允许添加、删除或定制特定的 AuthenticationProvider。
//                                .authenticationProviders(authenticationProvidersConsumer)
//                                //AuthenticationSuccessHandler（后处理器），用于处理成功的客户端认证，并将 OAuth2ClientAuthenticationToken 与 SecurityContext 相关联。
//                                .authenticationSuccessHandler(authenticationSuccessHandler)
//                                //AuthenticationFailureHandler（后处理器），用于处理客户端认证失败并返回 OAuth2Error 响应。
//                                .errorResponseHandler(errorResponseHandler)
//        )
 */
                //OAuth2授权端点 的 configurer。
                .authorizationEndpoint(authorizationEndpoint ->
                        authorizationEndpoint
/*
//                                //添加一个 AuthenticationConverter（预处理器），当试图从 HttpServletRequest 中提取 OAuth2授权请求（或consent）到 OAuth2AuthorizationCodeRequestAuthenticationToken 或 OAuth2AuthorizationConsentAuthenticationToken 的实例时使用。
//                                .authorizationRequestConverter(authorizationRequestConverter)
//                                //设置 Consumer，提供对默认和（可选）添加的 AuthenticationConverter List 的访问，允许添加、删除或定制特定的 AuthenticationConverter 的能力。
//                                .authorizationRequestConverters(authorizationRequestConvertersConsumer)
//                                //添加一个 AuthenticationProvider（主处理器），用于验证 OAuth2AuthorizationCodeRequestAuthenticationToken 或 OAuth2AuthorizationConsentAuthenticationToken。
//                                .authenticationProvider(authenticationProvider)
//                                //设置 Consumer，提供对默认和（可选）添加的 AuthenticationProvider List 的访问，允许添加、删除或定制特定的 AuthenticationProvider。
//                                .authenticationProviders(authenticationProvidersConsumer)
//                                //AuthenticationSuccessHandler（后处理器），用于处理 "已认证" 的 OAuth2AuthorizationCodeRequestAuthenticationToken 并返回 OAuth2AuthorizationResponse。
//                                .authorizationResponseHandler(authorizationResponseHandler)
//                                //AuthenticationFailureHandler（后处理器），用于处理 OAuth2AuthorizationCodeRequestAuthenticationException，并返回 OAuth2Error 响应。
//                                .errorResponseHandler(errorResponseHandler)
 */
                                //自定义同意（consent）页面的 URI，如果在授权请求流程中需要同意，则将资源所有者重定向至此。
                                .consentPage(AuthConstants.URL_OAUTH2_CONSENT)
        )
                //OAuth2令牌端点 的 configurer。
                .tokenEndpoint(tokenEndpoint ->
                        tokenEndpoint
/*
//                                //添加一个 AuthenticationConverter（预处理器），当试图从 HttpServletRequest 中提取 OAuth2 access token 请求 到 OAuth2AuthorizationGrantAuthenticationToken 的实例时使用。
//                                .accessTokenRequestConverter(accessTokenRequestConverter)
//                                //设置 Consumer，提供对默认和（可选）添加的 AuthenticationConverter List 的访问，允许添加、删除或定制特定的 AuthenticationConverter 的能力。
//                                .accessTokenRequestConverters(accessTokenRequestConvertersConsumer)
//                                //添加一个用于认证 OAuth2AuthorizationGrantAuthenticationToken 的 AuthenticationProvider（主处理器）。
//                                .authenticationProvider(authenticationProvider)
//                                //设置 Consumer，提供对默认和（可选）添加的 AuthenticationProvider List 的访问，允许添加、删除或定制特定的 AuthenticationProvider。
//                                .authenticationProviders(authenticationProvidersConsumer)
 */
//                                //用于处理 OAuth2AccessTokenAuthenticationToken 并返回 OAuth2AccessTokenResponse 的 AuthenticationSuccessHandler（后处理器）。
                                .accessTokenResponseHandler(authenticationSuccessEventHandler)
//                                //AuthenticationFailureHandler（后处理程序），用于处理 OAuth2AuthenticationException 并返回 OAuth2Error 响应。
                                .errorResponseHandler(authenticationFailureEventHandler)
       )
                /*
                    //OAuth2 Token Introspection endpoint 端点的 configurer。
//                .tokenIntrospectionEndpoint(tokenIntrospectionEndpoint ->
//                        tokenIntrospectionEndpoint
//                                //添加一个 AuthenticationConverter（预处理器），当试图从 HttpServletRequest 中提取 OAuth2 introspection 请求 到 OAuth2TokenIntrospectionAuthenticationToken 的实例时使用。
//                                .introspectionRequestConverter(introspectionRequestConverter)
//                                //设置 Consumer，提供对默认和（可选）添加的 AuthenticationConverter List 的访问，允许添加、删除或定制特定的 AuthenticationConverter 的能力。
//                                .introspectionRequestConverters(introspectionRequestConvertersConsumer)
//                                //添加一个用于认证 OAuth2TokenIntrospectionAuthenticationToken 的 AuthenticationProvider（主处理器）。
//                                .authenticationProvider(authenticationProvider)
//                                //设置 Consumer，提供对默认和（可选）添加的 AuthenticationProvider List 的访问，允许添加、删除或定制特定的 AuthenticationProvider。
//                                .authenticationProviders(authenticationProvidersConsumer)
//                                //AuthenticationSuccessHandler（后处理器），用于处理 "已认证" 的 OAuth2TokenIntrospectionAuthenticationToken 并返回 OAuth2TokenIntrospection 响应。
//                                .introspectionResponseHandler(introspectionResponseHandler)
//                                //AuthenticationFailureHandler（后处理程序），用于处理 OAuth2AuthenticationException 并返回 OAuth2Error 响应。
//                                .errorResponseHandler(errorResponseHandler)
//       )
                // OAuth2 Token Revocation 端点 的 configurer。
//                .tokenRevocationEndpoint(tokenRevocationEndpoint ->
//                        tokenRevocationEndpoint
//                                //添加一个 AuthenticationConverter（预处理器），在试图从 HttpServletRequest 中提取 OAuth2 revocation 请求 到 OAuth2TokenRevocationAuthenticationToken 的实例时使用。
//                                .revocationRequestConverter(revocationRequestConverter)
//                                //设置 Consumer ，提供对默认和（可选）添加的 AuthenticationConverter List 的访问，允许添加、删除或定制特定的 AuthenticationConverter 的能力。
//                                .revocationRequestConverters(revocationRequestConvertersConsumer)
//                                //添加一个用于认证 OAuth2TokenRevocationAuthenticationToken 的`AuthenticationProvider`（主处理器）。
//                                .authenticationProvider(authenticationProvider)
//                                //设置 Consumer，提供对默认和（可选）添加的 AuthenticationProvider List 的访问，允许添加、删除或定制特定的 AuthenticationProvider。
//                                .authenticationProviders(authenticationProvidersConsumer)
//                                //AuthenticationSuccessHandler（后处理器），用于处理 "已认证" 的 OAuth2TokenRevocationAuthenticationToken，并返回 OAuth2 revocation 响应。
//                                .revocationResponseHandler(revocationResponseHandler)
//                                //AuthenticationFailureHandler（后处理程序），用于处理 OAuth2AuthenticationException 并返回 OAuth2Error 响应。
//                                .errorResponseHandler(errorResponseHandler)
//        )
                //OAuth2授权服务器元数据端点 的 configurer。
//                .authorizationServerMetadataEndpoint(authorizationServerMetadataEndpoint ->
//                        authorizationServerMetadataEndpoint
//                                //提供访问 OAuth2AuthorizationServerMetadata.Builder 的 Consumer，允许自定义授权服务器的配置 claim 的能力。
//                                .authorizationServerMetadataCustomizer(authorizationServerMetadataCustomizer)
//        )
//                .oidc(oidc -> oidc
                        // OpenID Connect 1.0 Provider 配置端点 的 configurer。
//                        .providerConfigurationEndpoint(providerConfigurationEndpoint ->
//                                providerConfigurationEndpoint
//                                        //Consumer 提供对 OidcProviderConfiguration.Builder 的访问，允许自定义 OpenID Provider 的配置 claim 的能力。
//                                        .providerConfigurationCustomizer(providerConfigurationCustomizer)
//       )
                        // OpenID Connect 1.0 UserInfo 端点 的 configurer。
//                        .userInfoEndpoint(userInfoEndpoint ->
//                                userInfoEndpoint
//                                        //添加一个 AuthenticationConverter（预处理器），当试图从 HttpServletRequest 中提取 UserInfo 请求到 OidcUserInfoAuthenticationToken 的一个实例时使用。
//                                        .userInfoRequestConverter(userInfoRequestConverter)
//                                        //设置 Consumer，提供对默认和（可选）添加的 AuthenticationConverter List 的访问，允许添加、删除或定制特定的 AuthenticationConverter 的能力。
//                                        .userInfoRequestConverters(userInfoRequestConvertersConsumer)
//                                        //添加一个用于认证 OidcUserInfoAuthenticationToken 的 AuthenticationProvider（主处理器）。
//                                        .authenticationProvider(authenticationProvider)
//                                        //设置 Consumer，提供对默认和（可选）添加的 AuthenticationProvider List 的访问，允许添加、删除或定制特定的 AuthenticationProvider。
//                                        .authenticationProviders(authenticationProvidersConsumer)
//                                        //AuthenticationSuccessHandler（后处理器），用于处理 "已认证" 的 OidcUserInfoAuthenticationToken 并返回 UserInfo 响应。
//                                        .userInfoResponseHandler(userInfoResponseHandler)
//                                        //AuthenticationFailureHandler（后处理器），用于处理 OAuth2AuthenticationException 并返回 UserInfo Error响应。
//                                        .errorResponseHandler(errorResponseHandler)
//                                        //用于从 OidcUserInfoAuthenticationContext 向 OidcUserInfo 的实例提取 claim 的 Function。
//                                        .userInfoMapper(userInfoMapper)
//        )
                        // OpenID Connect 1.0 客户端注册端点 的 configurer。
//                        .clientRegistrationEndpoint(clientRegistrationEndpoint ->
//                                clientRegistrationEndpoint
//                                        //添加一个 AuthenticationConverter（预处理器），当试图从 HttpServletRequest 中提取 客户端注册请求 或 客户端读取请求 到 OidcClientRegistrationAuthenticationToken 的一个实例时使用。
//                                        .clientRegistrationRequestConverter(clientRegistrationRequestConverter)
//                                        //设置 Consumer，提供对默认和（可选）添加的 AuthenticationConverter List 的访问，允许添加、删除或定制特定的 AuthenticationConverter 的能力。
//                                        .clientRegistrationRequestConverters(clientRegistrationRequestConvertersConsumers)
//                                        //添加一个用于认证 OidcClientRegistrationAuthenticationToken 的 AuthenticationProvider（主处理器）。
//                                        .authenticationProvider(authenticationProvider)
//                                        //设置 Consumer，提供对默认和（可选）添加的 AuthenticationProvider List 的访问，允许添加、删除或定制特定的 AuthenticationProvider。
//                                        .authenticationProviders(authenticationProvidersConsumer)
//                                        //AuthenticationSuccessHandler（后处理器）用于处理 "已认证" 的 OidcClientRegistrationAuthenticationToken 并返回 客户端注册响应 或 客户端读取响应。
//                                        .clientRegistrationResponseHandler(clientRegistrationResponseHandler)
//                                        // AuthenticationFailureHandler（后处理器），用于处理 OAuth2AuthenticationException 并返回 客户端注册错误响应 或 客户端读取错误响应。
//                                        .errorResponseHandler(errorResponseHandler)
//       )
//                )
                 */
                ;

        http.exceptionHandling((exceptions) -> exceptions
                    .defaultAuthenticationEntryPointFor(
                            new CustomLoginUrlAuthenticationEntryPoint(AuthConstants.URL_OAUTH2_LOGIN),
                            new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                    )
            )
            .oauth2ResourceServer(oauth2ResourceServer ->
                    oauth2ResourceServer.jwt(Customizer.withDefaults()));

        return http.build();
    }

    /**
     * 	com.nimbusds.jose.jwk.source.JWKSource 的一个实例，用于签署访问令牌（access token）。
     */
    @Bean
    @SneakyThrows
    public JWKSource<SecurityContext> jwkSource() {
        // 尝试从Redis中获取JWKSet(JWT密钥对，包含非对称加密的公钥和私钥)
        String jwkSetStr = redisService.getValue(redisKeyGenerator.generateKey(RedisKeyModule.AUTH,AuthConstants.JWK_SET_KEY));
        if (StrUtil.isNotBlank(jwkSetStr)) {
            // 如果存在，解析JWKSet并返回
            JWKSet jwkSet = JWKSet.parse(jwkSetStr);
            return new ImmutableJWKSet<>(jwkSet);
        } else {
            // 如果Redis中不存在JWKSet，生成新的JWKSet
            KeyPair keyPair = generateRsaKey();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

            // 构建RSAKey
            RSAKey rsaKey = new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID(UUID.randomUUID().toString())
                    .build();

            // 构建JWKSet
            JWKSet jwkSet = new JWKSet(rsaKey);

            // 将JWKSet存储在Redis中
            redisService.setValue(redisKeyGenerator.generateKey(RedisKeyModule.AUTH,AuthConstants.JWK_SET_KEY),
                    jwkSet.toString(Boolean.FALSE));
            return new ImmutableJWKSet<>(jwkSet);
        }

    }

    /**
     * java.security.KeyPair 的一个实例，其 key 在启动时生成，用于创建上述 JWKSource。
     */
    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    /**
     * 	JwtDecoder 的一个实例，用于解码签名访问令牌（access token）。
     */
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     * 	AuthorizationServerSettings 的一个实例，用于配置Spring授权服务器。
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
/*
//                .issuer("https://example.com")
//                .authorizationEndpoint("/oauth2/v1/authorize")
//                .tokenEndpoint("/oauth2/v1/token")
//                .tokenIntrospectionEndpoint("/oauth2/v1/introspect")
//                .tokenRevocationEndpoint("/oauth2/v1/revoke")
//                .jwkSetEndpoint("/oauth2/v1/jwks")
//                .oidcUserInfoEndpoint("/connect/v1/userinfo")
//                .oidcClientRegistrationEndpoint("/connect/v1/register")
 */
                .build();
    }

    /**
     * 配置密码解析器，使用BCrypt的方式对密码进行加密和验证
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public OAuth2TokenGenerator<?> tokenGenerator(JWKSource<SecurityContext> jwkSource,OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer) {
        JwtGenerator jwtGenerator = new JwtGenerator(new NimbusJwtEncoder(jwkSource));
        jwtGenerator.setJwtCustomizer(jwtCustomizer);

        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
        return new DelegatingOAuth2TokenGenerator(
                jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
    }

    /**
     * JWT 自定义字段
     * @see <a href="https://docs.spring.io/spring-authorization-server/reference/guides/how-to-custom-claims-authorities.html">Add custom claims to JWT access tokens</a>
     */
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
        return context -> {
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType()) && context.getPrincipal() instanceof UsernamePasswordAuthenticationToken) {
                // Customize headers/claims for access_token
                Optional.ofNullable(context.getPrincipal().getPrincipal()).ifPresent(principal -> {
                    JwtClaimsSet.Builder claims = context.getClaims();

                    if (principal instanceof SysUserDetails userDetails) { // 系统用户添加自定义字段

                        claims.claim(JwtClaimConstants.USER_ID, userDetails.getUserId());
                        claims.claim(JwtClaimConstants.USERNAME, userDetails.getUsername());
                        claims.claim(JwtClaimConstants.DEPT_ID, userDetails.getDeptId());

                        // 这里存入角色至JWT，解析JWT的角色用于鉴权的位置: ResourceServerConfig#jwtAuthenticationConverter
                        var authorities = AuthorityUtils.authorityListToSet(context.getPrincipal().getAuthorities())
                                .stream()
                                .collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));
                        claims.claim(JwtClaimConstants.AUTHORITIES, authorities);

                    } else if (principal instanceof MemberDetails userDetails) {
                        // 会员添加自定义字段
                        claims.claim(JwtClaimConstants.MEMBER_ID, String.valueOf(userDetails.getId()));
                    }
                });
            }
        };
    }

    /**
     * 显示的生命manager对象，用于注入依赖
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
