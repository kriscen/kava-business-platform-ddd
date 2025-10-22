package com.kava.kbpd.auth.oauth2.component;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author Kris
 * @date 2025/10/13
 * @description: UsernamePasswordAuthenticationToken扩展
 */
@Getter
public class ExtendAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private String tenantId;

    private String userType;

    public ExtendAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public ExtendAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public ExtendAuthenticationToken(Object principal, Object credentials, String tenantId, String userType) {
        super(principal, credentials);
        this.tenantId = tenantId;
        this.userType = userType;
    }

    public ExtendAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String tenantId, String userType) {
        super(principal, credentials, authorities);
        this.tenantId = tenantId;
        this.userType = userType;
    }
}
