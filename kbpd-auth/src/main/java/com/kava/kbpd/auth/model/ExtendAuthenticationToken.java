package com.kava.kbpd.auth.model;

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

    private final String tenantId;

    private final String userType;

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

    /**
     * This factory method can be safely used by any code that wishes to create a
     * unauthenticated <code>UsernamePasswordAuthenticationToken</code>.
     * @param principal
     * @param credentials
     * @return UsernamePasswordAuthenticationToken with false isAuthenticated() result
     *
     * @since 5.7
     */
    public static ExtendAuthenticationToken unauthenticated(Object principal, Object credentials, String tenantId, String userType) {
        return new ExtendAuthenticationToken(principal, credentials,tenantId, userType);
    }

    /**
     * This factory method can be safely used by any code that wishes to create a
     * authenticated <code>UsernamePasswordAuthenticationToken</code>.
     * @param principal
     * @param credentials
     * @return UsernamePasswordAuthenticationToken with true isAuthenticated() result
     *
     * @since 5.7
     */
    public static ExtendAuthenticationToken authenticated(Object principal, Object credentials,
                                                            Collection<? extends GrantedAuthority> authorities,
                                                          String tenantId, String userType) {
        return new ExtendAuthenticationToken(principal, credentials, authorities,tenantId, userType);
    }
}
