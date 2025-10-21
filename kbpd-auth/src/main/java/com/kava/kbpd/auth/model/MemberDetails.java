package com.kava.kbpd.auth.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


/**
 * @author Kris
 * @date 2025/4/14
 * @description: 会员信息
 */
@Data
public class MemberDetails implements UserDetails {

    /**
     * 会员ID
     */
    private Long id;

    /**
     * 会员用户名(openid/mobile)
     */
    private String username;

    private String password;

    /**
     * 会员状态
     */
    private Boolean enabled;

    public MemberDetails(Long id, String username, String password, Boolean enabled) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }
}
