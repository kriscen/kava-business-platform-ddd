package com.kava.kbpd.common.security.dubbo;

import com.kava.kbpd.common.core.model.UserContext;
import com.kava.kbpd.common.security.context.UserContextHolder;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Dubbo Provider 端 Filter：从 RpcContext attachment 读取用户上下文设置到 UserContextHolder，调用完成后清理
 */
@Activate(group = CommonConstants.PROVIDER)
public class ProviderUserContextFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            String tenantIdStr = RpcContext.getServerAttachment().getAttachment("ctx_tenantId");
            if (tenantIdStr != null) {
                UserContext userContext = UserContext.builder()
                        .tenantId(parseLong(tenantIdStr))
                        .userType(RpcContext.getServerAttachment().getAttachment("ctx_userType"))
                        .userId(parseLong(RpcContext.getServerAttachment().getAttachment("ctx_userId")))
                        .memberId(parseLong(RpcContext.getServerAttachment().getAttachment("ctx_memberId")))
                        .username(RpcContext.getServerAttachment().getAttachment("ctx_username"))
                        .deptId(parseLong(RpcContext.getServerAttachment().getAttachment("ctx_deptId")))
                        .roles(parseRoles(RpcContext.getServerAttachment().getAttachment("ctx_roles")))
                        .dataScope(RpcContext.getServerAttachment().getAttachment("ctx_dataScope"))
                        .permissions(parseRoles(RpcContext.getServerAttachment().getAttachment("ctx_permissions")))
                        .build();
                UserContextHolder.set(userContext);
            }
            return invoker.invoke(invocation);
        } finally {
            UserContextHolder.clear();
        }
    }

    private Long parseLong(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Set<String> parseRoles(String value) {
        if (value == null || value.isEmpty()) {
            return Collections.emptySet();
        }
        return Arrays.stream(value.split(","))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }
}
