package com.kava.kbpd.auth.oauth2.component;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

public class ExtendAuthenticationConverter implements AuthenticationConverter {

    @Override
    public Authentication convert(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String tenantId = request.getParameter("tenantId");
        String userType = request.getParameter("userType");

        return new ExtendUsernamePasswordAuthenticationToken(username, password, tenantId, 1);
    }
}