package com.kava.kbpd.auth.oauth2.component;

import com.kava.kbpd.auth.model.SysUserDetails;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UsernameNotFoundException("user not found");
    }

    public UserDetails loadUserByUsername(String mobile,String tenantId,Integer userType) throws UsernameNotFoundException {
        return new SysUserDetails(1L, "admin", "{noop}123456", 1L, true, Set.of());
    }
}
