package com.kava.kbpd.auth.handler;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.oauth2.SaOAuth2Manager;
import cn.dev33.satoken.oauth2.data.model.AccessTokenModel;
import cn.dev33.satoken.oauth2.data.model.request.RequestAuthModel;
import cn.dev33.satoken.oauth2.exception.SaOAuth2Exception;
import cn.dev33.satoken.oauth2.granttype.handler.SaOAuth2GrantTypeHandlerInterface;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import com.kava.kbpd.common.core.constants.CoreConstant;
import com.kava.kbpd.common.security.constants.SecurityConstants;
import com.kava.kbpd.common.security.enums.AuthRedisKeyType;
import com.kava.kbpd.common.security.enums.CustomerGrantType;
import com.kava.kbpd.common.security.enums.UserType;
import com.kava.kbpd.member.api.model.dto.MemberInfoDTO;
import com.kava.kbpd.member.api.service.IRemoteMemberService;
import com.kava.kbpd.upms.api.model.dto.SysUserDTO;
import com.kava.kbpd.upms.api.service.IRemoteUserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Kris
 * @date 2025/4/3
 * @description: sms 登录处理
 */
@Component
public class SmsGrantTypeHandler implements SaOAuth2GrantTypeHandlerInterface {

    @DubboReference(version = "1.0")
    private IRemoteUserService remoteUserService;

    @DubboReference(version = "1.0")
    private IRemoteMemberService remoteMemberService;

    @Override
    public String getHandlerGrantType() {
        return CustomerGrantType.SMS.getCode();
    }

    @Override
    public AccessTokenModel getAccessToken(SaRequest req, String clientId, List<String> scopes) {
        // 获取前端提交的参数
        String mobile = req.getParamNotNull(SecurityConstants.PARAM_MOBILE);
        String code = req.getParamNotNull(SecurityConstants.PARAM_SMS_CODE);
        String realCode = SaManager.getSaTokenDao().get(AuthRedisKeyType.SMS_CODE + CoreConstant.SEPARATOR + mobile);

        // 1、校验验证码是否正确
        if(!code.equals(realCode)) {
            throw new SaOAuth2Exception("验证码错误");
        }

        // 2、校验通过，删除验证码
        SaManager.getSaTokenDao().delete(AuthRedisKeyType.SMS_CODE + CoreConstant.SEPARATOR + mobile);

        //默认获取用户类型放在扩展属性中 默认c端用户
        String userType = req.getHeader(SecurityConstants.HTTP_HEARD_USER_TYPE, UserType.TO_C.getCode());

        // 3、登录
        Long userId;
        if (UserType.TO_C.getCode().equals(userType)) {
            MemberInfoDTO member = remoteMemberService.findMemberByMobile(mobile);
            userId = member.getId();
        } else {
            SysUserDTO user = remoteUserService.findByUsername(mobile);
            userId = user.getId();
        }

        SaOAuth2Manager.getStpLogic().login(userId,
                new SaLoginParameter().setExtra(SecurityConstants.JWT_USER_TYPE,userType));

        // 4、构建 ra 对象
        RequestAuthModel ra = new RequestAuthModel();
        ra.clientId = clientId;
        ra.loginId = userId;
        ra.scopes = scopes;

        // 5、生成 Access-Token
        return SaOAuth2Manager.getDataGenerate()
                .generateAccessToken(ra, true,
                        atm -> {
                    atm.grantType = CustomerGrantType.SMS.getCode();
                });
    }
}
