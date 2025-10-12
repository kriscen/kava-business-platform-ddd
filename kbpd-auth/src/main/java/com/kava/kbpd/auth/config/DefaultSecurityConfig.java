package com.kava.kbpd.auth.config;

import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.databind.Module;
import com.kava.kbpd.auth.constants.AuthConstants;
import com.kava.kbpd.auth.oauth2.jackson.CustomerOauth2Module;
import com.kava.kbpd.auth.oauth2.jackson.CustomerUserDetailsModule;
import lombok.Setter;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;

/**
 * @author Kris
 * @date 2025/4/14
 * @description: 默认安全配置
 */
@Setter
@ConfigurationProperties(prefix = "kbpd.auth")
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class DefaultSecurityConfig {

    /**
     * 白名单路径列表
     */
    private List<String> whitelistPaths;

    /**
     * Spring Security 安全过滤器链配置
     *
     * @param http 安全配置
     * @return 安全过滤器链
     */
    @Bean
    @Order(10)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
        http.authorizeHttpRequests((requests) ->
                        {
                            if (CollectionUtil.isNotEmpty(whitelistPaths)) {
                                for (String whitelistPath : whitelistPaths) {
                                    requests.requestMatchers(mvcMatcherBuilder.pattern(whitelistPath)).permitAll();
                                }
                            }
                            requests.anyRequest().authenticated();
                        }
                )
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(formLogin -> 
                    formLogin
                        .loginPage(AuthConstants.URL_OAUTH2_LOGIN)  // 设置自定义登录页面
                        .loginProcessingUrl("/login")
                        .permitAll()
                        .failureUrl(AuthConstants.URL_OAUTH2_ERROR)  // 登录失败时重定向
                );

        return http.build();
    }


    /**
     * 不走过滤器链的放行配置
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                AntPathRequestMatcher.antMatcher("/webjars/**"),
                AntPathRequestMatcher.antMatcher("/doc.html"),
                AntPathRequestMatcher.antMatcher("/swagger-resources/**"),
                AntPathRequestMatcher.antMatcher("/v3/api-docs/**"),
                AntPathRequestMatcher.antMatcher("/swagger-ui/**"),
                AntPathRequestMatcher.antMatcher(HttpMethod.GET,"/oauth2/login"),
                AntPathRequestMatcher.antMatcher(HttpMethod.GET,"/oauth2/error"),
                AntPathRequestMatcher.antMatcher("/assets/**")
        );
    }


    @Bean
    public Jackson2ObjectMapperBuilderCustomizer securityJacksonCustomizer() {
        return builder -> {
            // 获取Spring Security的Jackson模块并注册
            List<Module> securityModules = SecurityJackson2Modules.getModules(
                    DefaultSecurityConfig.class.getClassLoader() // 传入类加载器
            );
            securityModules.add(new OAuth2AuthorizationServerJackson2Module());

            // 2. 创建自定义模块并注册反序列化器
            securityModules.add(new CustomerOauth2Module());
            securityModules.add(new CustomerUserDetailsModule());
            // 使用modulesToInstall()追加模块，而非覆盖
            builder.modulesToInstall(securityModules.toArray(new Module[0]));
        };
    }

}
