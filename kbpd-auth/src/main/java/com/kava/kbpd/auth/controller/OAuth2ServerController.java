package com.kava.kbpd.auth.controller;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.oauth2.processor.SaOAuth2ServerProcessor;
import cn.dev33.satoken.util.SaFoxUtil;
import com.kava.kbpd.common.core.base.JsonResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kris
 * @date 2025/4/3
 * @description: OAuth2 controller
 */
@RestController
public class OAuth2ServerController {

    // 模式一：Code授权码 || 模式二：隐藏式
    @RequestMapping("/oauth2/authorize")
    public Object authorize() {
        return SaOAuth2ServerProcessor.instance.authorize();
    }

    // 用户登录
    @RequestMapping("/oauth2/doLogin")
    public Object doLogin() {
        return SaOAuth2ServerProcessor.instance.doLogin();
    }

    // 用户确认授权
    @RequestMapping("/oauth2/doConfirm")
    public Object doConfirm() {
        return SaOAuth2ServerProcessor.instance.doConfirm();
    }

    // Code 换 Access-Token || 模式三：密码式
    @RequestMapping("/oauth2/token")
    public Object token() {
        return SaOAuth2ServerProcessor.instance.token();
    }

    // Refresh-Token 刷新 Access-Token
    @RequestMapping("/oauth2/refresh")
    public Object refresh() {
        return SaOAuth2ServerProcessor.instance.refresh();
    }

    // 回收 Access-Token
    @RequestMapping("/oauth2/revoke")
    public Object revoke() {
        return SaOAuth2ServerProcessor.instance.revoke();
    }

    // 模式四：凭证式
    @RequestMapping("/oauth2/client_token")
    public Object clientToken() {
        return SaOAuth2ServerProcessor.instance.clientToken();
    }

    @RequestMapping("/oauth2/smsCode")
    public JsonResult<Void> sendCode(String phone) {
        String code = SaFoxUtil.getRandomNumber(100000, 999999) + "";
        SaManager.getSaTokenDao().set("phone_code:" + phone, code, 60 * 5 * 1000);
        System.out.println("手机号：" + phone + "，验证码：" + code + "，已发送成功");
        return JsonResult.buildSuccess();
    }
}