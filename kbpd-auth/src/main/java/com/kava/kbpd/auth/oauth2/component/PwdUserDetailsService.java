package com.kava.kbpd.auth.oauth2.component;

import com.kava.kbpd.auth.enums.UserType;
import com.kava.kbpd.auth.model.MemberDetails;
import com.kava.kbpd.auth.model.SysUserDetails;
import com.kava.kbpd.member.api.model.dto.MemberInfoDTO;
import com.kava.kbpd.member.api.service.IRemoteMemberService;
import com.kava.kbpd.upms.api.model.dto.SysUserDTO;
import com.kava.kbpd.upms.api.service.IRemoteOauthClientService;
import com.kava.kbpd.upms.api.service.IRemoteUserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author Kris
 * @date 2025/4/14
 * @description:
 */
@Service
public class PwdUserDetailsService implements UserDetailsService {

    @DubboReference(version = "1.0")
    private IRemoteUserService remoteUserService;

    @DubboReference(version = "1.0")
    private IRemoteMemberService remoteMemberService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UsernameNotFoundException("user not found");
    }

    public UserDetails loadUserByUsername(String username,String tenantId,String userType) throws UsernameNotFoundException {
        if(UserType.TO_B.getCode().equals(userType)){
            SysUserDTO user = remoteUserService.findByUsername(username, tenantId);
            return new SysUserDetails(user.getId(), "admin", "{noop}123456", 1L, true, Set.of());
        }else if(UserType.TO_C.getCode().equals(userType)) {
            MemberInfoDTO member = remoteMemberService.findMemberByMobile(username, tenantId);
            return new MemberDetails(member.getId(), "admin", "{noop}123456", true);
        }else {
            throw new UsernameNotFoundException("user not found");
        }

    }
}
