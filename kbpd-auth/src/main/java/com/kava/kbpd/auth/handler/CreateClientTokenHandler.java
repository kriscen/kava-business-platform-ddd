package com.kava.kbpd.auth.handler;

import cn.dev33.satoken.oauth2.function.strategy.SaOAuth2CreateAccessTokenValueFunction;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Kris
 * @date 2025/4/9
 * @description: client token handler
 */
@Component
public class CreateClientTokenHandler implements SaOAuth2CreateAccessTokenValueFunction {

    @Override
    public String execute(String clientId, Object loginId, List<String> scopes) {
        return StpUtil.getOrCreateLoginSession(loginId);
    }
}
