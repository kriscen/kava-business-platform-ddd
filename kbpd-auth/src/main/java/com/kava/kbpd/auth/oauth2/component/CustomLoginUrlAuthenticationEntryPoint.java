package com.kava.kbpd.auth.oauth2.component;

import com.kava.kbpd.auth.constants.AuthConstants;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.log.LogMessage;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.RedirectUrlBuilder;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CustomLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    private static final Log logger = LogFactory.getLog(CustomLoginUrlAuthenticationEntryPoint.class);

    private PortMapper portMapper = new PortMapperImpl();

    private PortResolver portResolver = new PortResolverImpl();

    @Getter
    private final String loginFormUrl;

    @Setter
    private boolean forceHttps = false;

    @Setter
    private boolean useForward = false;

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    /**
     * @param loginFormUrl URL where the login page can be found. Should either be
     * relative to the web-app context path (include a leading {@code /}) or an absolute
     * URL.
     */
    public CustomLoginUrlAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
        Assert.notNull(loginFormUrl, "loginFormUrl cannot be null");
        this.loginFormUrl = loginFormUrl;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.isTrue(StringUtils.hasText(this.loginFormUrl) && UrlUtils.isValidRedirectUrl(this.loginFormUrl),
                "loginFormUrl must be specified and must be a valid redirect URL");
        Assert.isTrue(!this.useForward || !UrlUtils.isAbsoluteUrl(this.loginFormUrl),
                "useForward must be false if using an absolute loginFormURL");
        Assert.notNull(this.portMapper, "portMapper must be specified");
        Assert.notNull(this.portResolver, "portResolver must be specified");
    }

    /**
     * Allows subclasses to modify the login form URL that should be applicable for a
     * given request.
     * @param request the request
     * @param response the response
     * @param exception the exception
     * @return the URL (cannot be null or empty; defaults to {@link #getLoginFormUrl()})
     */
    protected String determineUrlToUseForThisRequest(HttpServletRequest request, HttpServletResponse response,
                                                     AuthenticationException exception) {
        return getLoginFormUrl();
    }

    /**
     * Performs the redirect (or forward) to the login form URL.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // 提取自定义参数
        String userType = request.getParameter(AuthConstants.URL_PARAM_USER_TYPE);
        String tenantId = request.getParameter(AuthConstants.URL_PARAM_TENANT_ID);

        // 构建要携带的额外参数字符串（如 "?userType=admin&tenantId=123"）
        String extraParams = buildExtraParameters(userType, tenantId);

        if (!this.useForward) {
            // 情况1：执行重定向（Redirect）→ 跳转到登录页
            String redirectUrl = buildRedirectUrlToLoginPage(request, response, authException);

            //在重定向 URL 后拼接自定义参数
            if (StringUtils.hasText(extraParams)) {
                redirectUrl = appendQueryParams(redirectUrl, extraParams);
            }

            this.redirectStrategy.sendRedirect(request, response, redirectUrl);
            return;
        }

        String redirectUrl = null;
        if (this.forceHttps && "http".equals(request.getScheme())) {
            // 情况2：强制 HTTPS → 先跳转到 HTTPS 版本
            redirectUrl = buildHttpsRedirectUrlForRequest(request);

            //在 HTTPS 跳转时也带上参数
            if (StringUtils.hasText(extraParams)) {
                redirectUrl = appendQueryParams(redirectUrl, extraParams);
            }

            this.redirectStrategy.sendRedirect(request, response, redirectUrl);
            return;
        }

        // 情况3：服务端 Forward（不改变浏览器地址栏）
        String loginForm = determineUrlToUseForThisRequest(request, response, authException);

        //如果是 forward，也需要把参数带过去（否则参数会丢失）
        if (StringUtils.hasText(extraParams)) {
            // 注意：forward 不支持直接带 query string？其实是支持的！
            // 只要你在 web.xml 或 Servlet 映射中允许即可
            loginForm = appendQueryParams(loginForm, extraParams);
        }

        logger.debug(LogMessage.format("Server side forward to: %s", loginForm));
        RequestDispatcher dispatcher = request.getRequestDispatcher(loginForm);
        dispatcher.forward(request, response);
    }

    protected String buildRedirectUrlToLoginPage(HttpServletRequest request, HttpServletResponse response,
                                                 AuthenticationException authException) {
        String loginForm = determineUrlToUseForThisRequest(request, response, authException);
        if (UrlUtils.isAbsoluteUrl(loginForm)) {
            return loginForm;
        }
        int serverPort = this.portResolver.getServerPort(request);
        String scheme = request.getScheme();
        RedirectUrlBuilder urlBuilder = new RedirectUrlBuilder();
        urlBuilder.setScheme(scheme);
        urlBuilder.setServerName(request.getServerName());
        urlBuilder.setPort(serverPort);
        urlBuilder.setContextPath(request.getContextPath());
        urlBuilder.setPathInfo(loginForm);
        if (this.forceHttps && "http".equals(scheme)) {
            Integer httpsPort = this.portMapper.lookupHttpsPort(serverPort);
            if (httpsPort != null) {
                // Overwrite scheme and port in the redirect URL
                urlBuilder.setScheme("https");
                urlBuilder.setPort(httpsPort);
            }
            else {
                logger.warn(LogMessage.format("Unable to redirect to HTTPS as no port mapping found for HTTP port %s",
                        serverPort));
            }
        }
        return urlBuilder.getUrl();
    }

    /**
     * Builds a URL to redirect the supplied request to HTTPS. Used to redirect the
     * current request to HTTPS, before doing a forward to the login page.
     */
    protected String buildHttpsRedirectUrlForRequest(HttpServletRequest request) throws IOException, ServletException {
        int serverPort = this.portResolver.getServerPort(request);
        Integer httpsPort = this.portMapper.lookupHttpsPort(serverPort);
        if (httpsPort != null) {
            RedirectUrlBuilder urlBuilder = new RedirectUrlBuilder();
            urlBuilder.setScheme("https");
            urlBuilder.setServerName(request.getServerName());
            urlBuilder.setPort(httpsPort);
            urlBuilder.setContextPath(request.getContextPath());
            urlBuilder.setServletPath(request.getServletPath());
            urlBuilder.setPathInfo(request.getPathInfo());
            urlBuilder.setQuery(request.getQueryString());
            return urlBuilder.getUrl();
        }
        // Fall through to server-side forward with warning message
        logger.warn(
                LogMessage.format("Unable to redirect to HTTPS as no port mapping found for HTTP port %s", serverPort));
        return null;
    }


    protected boolean isForceHttps() {
        return this.forceHttps;
    }

    public void setPortMapper(PortMapper portMapper) {
        Assert.notNull(portMapper, "portMapper cannot be null");
        this.portMapper = portMapper;
    }

    protected PortMapper getPortMapper() {
        return this.portMapper;
    }

    public void setPortResolver(PortResolver portResolver) {
        Assert.notNull(portResolver, "portResolver cannot be null");
        this.portResolver = portResolver;
    }

    protected PortResolver getPortResolver() {
        return this.portResolver;
    }

    protected boolean isUseForward() {
        return this.useForward;
    }

    /**
     * 构建额外参数字符串，例如 "?userType=xxx&tenantId=yyy"
     */
    private String buildExtraParameters(String userType, String tenantId) {
        StringBuilder sb = new StringBuilder();
        boolean hasParam = false;

        if (StringUtils.hasText(userType)) {
            sb.append(AuthConstants.URL_PARAM_USER_TYPE).append("=").append(URLEncoder.encode(userType, StandardCharsets.UTF_8));
            hasParam = true;
        }
        if (StringUtils.hasText(tenantId)) {
            if (hasParam) {
                sb.append("&");
            }
            sb.append(AuthConstants.URL_PARAM_TENANT_ID).append("=")
                    .append(URLEncoder.encode(tenantId, StandardCharsets.UTF_8));
        }

        return !sb.isEmpty() ? "?" + sb.toString() : "";
    }

    /**
     * 将参数追加到目标 URL 上
     * 处理已有 ? 的情况
     */
    private String appendQueryParams(String url, String params) {
        if (!StringUtils.hasText(params)) {
            return url;
        }

        if (url == null) {
            return params;
        }

        String result = url;
        if (url.contains("?")) {
            if (url.endsWith("?") || url.endsWith("&")) {
                result = url + params.substring(1); // 去掉 params 的 '?'
            } else {
                result = url + "&" + params.substring(1);
            }
        } else {
            result = url + params; // 直接拼接 ?xxx=yyy
        }

        return result;
    }
}