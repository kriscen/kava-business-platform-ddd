package com.kava.kbpd.auth.handler;

import cn.dev33.satoken.oauth2.function.SaOAuth2NotLoginViewFunction;
import com.kava.kbpd.common.security.constants.SecurityConstants;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Kris
 * @date 2025/4/8
 * @description: 未登录视图处理Handler
 */
@Component
public class NotLoginViewHandler implements SaOAuth2NotLoginViewFunction {
    @Override
    public Object get() {
        return new ModelAndView(SecurityConstants.OAUTH2_VIEW_LOGIN_URL);
    }
}
