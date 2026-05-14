package com.kava.kbpd.common.database.mybatis;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.kava.kbpd.common.core.model.UserContext;
import com.kava.kbpd.common.security.context.UserContextHolder;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * 多租户拦截器：自动注入 WHERE tenant_id = ?
 * 平台管理员跳过租户过滤
 */
public class KavaTenantLineInnerInterceptor implements TenantLineHandler {

    /**
     * 忽略租户隔离的表列表
     */
    private final Set<String> ignoreTables;

    public KavaTenantLineInnerInterceptor(Set<String> ignoreTables) {
        this.ignoreTables = ignoreTables;
    }

    @Override
    public Expression getTenantId() {
        UserContext ctx = UserContextHolder.get();
        if (ctx != null && ctx.getTenantId() != null) {
            return new LongValue(ctx.getTenantId());
        }
        return new NullValue();
    }

    @Override
    public String getTenantIdColumn() {
        return "tenant_id";
    }

    @Override
    public boolean ignoreTable(String tableName) {
        // 忽略指定表
        if (ignoreTables != null && ignoreTables.contains(tableName)) {
            return true;
        }
        // 平台管理员跳过租户过滤
        UserContext ctx = UserContextHolder.get();
        if (ctx != null && isAdmin(ctx)) {
            return true;
        }
        return false;
    }

    private boolean isAdmin(UserContext ctx) {
        return ctx.getRoles() != null && ctx.getRoles().contains("ROLE_ADMIN");
    }
}
