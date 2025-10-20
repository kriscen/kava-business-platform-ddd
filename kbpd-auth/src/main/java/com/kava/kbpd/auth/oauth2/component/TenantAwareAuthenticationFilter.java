package com.kava.kbpd.auth.oauth2.component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class TenantAwareAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public TenantAwareAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response)
            throws AuthenticationException {

        if (!"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException("Only POST supported");
        }

        String username = obtainUsername(request);
        String password = obtainPassword(request);
        String tenantId = request.getParameter("tenantId");
        String userType = request.getParameter("userType");

        username = username != null ? username.trim() : "";
        password = password != null ? password : "";

        ExtendUsernamePasswordAuthenticationToken authRequest =
                new ExtendUsernamePasswordAuthenticationToken(
                        username, password, tenantId, 1);

        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }
}