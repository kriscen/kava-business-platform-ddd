package com.kava.kbpd.auth.handler;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.oauth2.SaOAuth2Manager;
import cn.dev33.satoken.oauth2.function.SaOAuth2DoLoginHandleFunction;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import cn.dev33.satoken.util.SaResult;
import com.kava.kbpd.common.security.constants.SecurityConstants;
import com.kava.kbpd.common.security.enums.UserType;
import com.kava.kbpd.member.api.model.dto.MemberInfoDTO;
import com.kava.kbpd.member.api.service.IRemoteMemberService;
import com.kava.kbpd.upms.api.model.dto.SysUserDTO;
import com.kava.kbpd.upms.api.service.IRemoteUserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @author Kris
 * @date 2025/4/7
 * @description: 默认账号密码类型的登录处理器
 */
@Component
public class PwdLoginHandler implements SaOAuth2DoLoginHandleFunction {

    @DubboReference(version = "1.0")
    private IRemoteUserService remoteUserService;

    @DubboReference(version = "1.0")
    private IRemoteMemberService remoteMemberService;

    @Override
    public Object apply(String name, String pwd) {
        SaRequest req = SaHolder.getRequest();
        //默认获取用户类型放在扩展属性中
        String userType = req.getHeader(SecurityConstants.HTTP_HEARD_USER_TYPE, UserType.TO_C.getCode());

        // 3、登录
        Long userId;
        if (UserType.TO_C.getCode().equals(userType)) {
            MemberInfoDTO member = remoteMemberService.loginByPwd(name,pwd);
            userId = member.getId();
        } else {
            SysUserDTO user = remoteUserService.loginByPwd(name,pwd);
            userId = user.getId();
        }
        SaOAuth2Manager.getStpLogic().login(userId,
                new SaLoginParameter().setExtra(SecurityConstants.JWT_USER_TYPE,userType));
        return SaResult.ok().set("satoken", SaOAuth2Manager.getStpLogic().getTokenValue());
    }
}
