package com.kava.kbpd.auth.model;

import lombok.Data;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;


/**
 * @author Kris
 * @date 2025/4/14
 * @description: 系统用户信息
 */
@Data
public class SysUserDetails implements UserDetails, CredentialsContainer {

    /**
     * 扩展字段：用户ID
     */
    private Long userId;

    /**
     * 扩展字段：部门ID
     */
    private Long deptId;

    /**
     * 默认字段
     */
    private String username;

    private String password;

    private Boolean enabled;

    private Collection<GrantedAuthority> authorities;

    private Set<String> perms;

    public SysUserDetails(Long userId,String username,String password,
            Long deptId,boolean enabled,Set<? extends GrantedAuthority> authorities
    ) {
        Assert.isTrue(username != null && !username.isEmpty() && password != null,
                "Cannot pass null or empty values to constructor");
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.deptId = deptId;
        this.enabled = enabled;
        this.authorities = Collections.unmodifiableSet(authorities);
    }

    @Override
    public void eraseCredentials() {

    }
}
