package com.kava.kbpd.auth.oauth2.component;

import com.kava.kbpd.auth.constants.AuthConstants;
import com.kava.kbpd.auth.model.ExtendAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class TenantAwareAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private RegisteredClientRepository registeredClientRepository;

    @Autowired
    @Lazy
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Autowired
    public void setRegisteredClientRepository(RegisteredClientRepository registeredClientRepository) {
        this.registeredClientRepository = registeredClientRepository;
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
        String clientId = request.getParameter("clientId");

        // 从已注册的 Client 配置中获取可信的 tenantId 和 userType
        RegisteredClient client = registeredClientRepository.findByClientId(clientId);
        if (client == null) {
            throw new AuthenticationServiceException("Invalid client: " + clientId);
        }

        String tenantId = client.getClientSettings()
                .getSetting(AuthConstants.URL_PARAM_TENANT_ID);
        String userType = client.getClientSettings()
                .getSetting(AuthConstants.URL_PARAM_USER_TYPE);

        username = username != null ? username.trim() : "";
        password = password != null ? password : "";

        ExtendAuthenticationToken authRequest =
                new ExtendAuthenticationToken(
                        username, password,
                        tenantId != null ? String.valueOf(tenantId) : null,
                        userType != null ? String.valueOf(userType) : null);

        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
