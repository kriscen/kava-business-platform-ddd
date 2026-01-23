package com.kava.kbpd.auth.oauth2.component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class TenantAwareAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    @Lazy
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
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
        String clientId = request.getParameter("clientId");
        //TODO check userType tenantId clientId是否匹配

        username = username != null ? username.trim() : "";
        password = password != null ? password : "";

        ExtendAuthenticationToken authRequest =
                new ExtendAuthenticationToken(
                        username, password, tenantId, userType);

        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }
}