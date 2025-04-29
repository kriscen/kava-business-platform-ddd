package com.kava.kbpd.auth.config;

import cn.dev33.satoken.oauth2.config.SaOAuth2ServerConfig;
import cn.dev33.satoken.oauth2.strategy.SaOAuth2Strategy;
import com.kava.kbpd.auth.handler.ConfirmViewHandler;
import com.kava.kbpd.auth.handler.CreateClientTokenHandler;
import com.kava.kbpd.auth.handler.NotLoginViewHandler;
import com.kava.kbpd.auth.handler.PwdLoginHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kris
 * @date 2025/4/3
 * @description: oauth2 server端配置
 */
@Configuration
public class Oauth2ServerConfig {

    @Autowired
    public void configOAuth2Server(SaOAuth2ServerConfig oauth2Server, PwdLoginHandler pwdLoginHandler,
                                   ConfirmViewHandler confirmViewHandler, NotLoginViewHandler notLoginViewHandler,
                                   CreateClientTokenHandler createClientTokenHandler) {
        // 未登录的视图
        oauth2Server.notLoginView = notLoginViewHandler;

        // 登录处理函数
        oauth2Server.doLoginHandle = pwdLoginHandler;

        // 授权确认视图
        oauth2Server.confirmView = confirmViewHandler;

        // 重写 AccessToken 创建策略，返回会话令牌
        SaOAuth2Strategy.instance.createAccessToken = createClientTokenHandler;

    }

}
