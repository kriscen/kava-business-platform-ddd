package com.kava.kbpd.common.security.dubbo;

import com.kava.kbpd.common.core.model.UserContext;
import com.kava.kbpd.common.security.context.UserContextHolder;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * Dubbo Consumer 端 Filter：从 UserContextHolder 提取 UserContext 写入 RpcContext attachment
 */
@Activate(group = CommonConstants.CONSUMER)
public class ConsumerUserContextFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        UserContext userContext = UserContextHolder.get();
        if (userContext != null) {
            RpcContext.getClientAttachment()
                    .setAttachment("ctx_tenantId", userContext.getTenantId() != null ? String.valueOf(userContext.getTenantId()) : null)
                    .setAttachment("ctx_userType", userContext.getUserType())
                    .setAttachment("ctx_userId", userContext.getUserId() != null ? String.valueOf(userContext.getUserId()) : null)
                    .setAttachment("ctx_memberId", userContext.getMemberId() != null ? String.valueOf(userContext.getMemberId()) : null)
                    .setAttachment("ctx_username", userContext.getUsername())
                    .setAttachment("ctx_deptId", userContext.getDeptId() != null ? String.valueOf(userContext.getDeptId()) : null)
                    .setAttachment("ctx_roles", userContext.getRoles() != null ? String.join(",", userContext.getRoles()) : null)
                    .setAttachment("ctx_dataScope", userContext.getDataScope())
                    .setAttachment("ctx_permissions", userContext.getPermissions() != null ? String.join(",", userContext.getPermissions()) : null);
        }
        return invoker.invoke(invocation);
    }
}
