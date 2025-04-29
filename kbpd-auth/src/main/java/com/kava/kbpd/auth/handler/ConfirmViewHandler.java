package com.kava.kbpd.auth.handler;

import cn.dev33.satoken.oauth2.function.SaOAuth2ConfirmViewFunction;
import com.kava.kbpd.common.security.constants.SecurityConstants;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kris
 * @date 2025/4/8
 * @description: 确认页视图处理
 */
@Component
public class ConfirmViewHandler implements SaOAuth2ConfirmViewFunction{
    @Override
    public Object apply(String clientId, List<String> scopes) {
        Map<String, Object> map = new HashMap<>();
        map.put("clientId", clientId);
        map.put("scope", scopes);
        return new ModelAndView(SecurityConstants.OAUTH2_VIEW_CONFIRM_URL, map);
    }
}
