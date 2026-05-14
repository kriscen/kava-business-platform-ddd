package com.kava.kbpd.common.database.scope;

import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.kava.kbpd.common.core.model.UserContext;
import com.kava.kbpd.common.security.context.UserContextHolder;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.schema.Column;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;

/**
 * 数据权限拦截器：根据当前用户角色的 dsType 策略拼接 SQL 条件
 *
 * dsType 策略：
 * 0=ALL（全部数据）
 * 1=CUSTOM（自定义，由 dsScope 指定部门ID）
 * 2=DEPT_AND_CHILD（本部门及子部门）
 * 3=DEPT_ONLY（仅本部门）
 * 4=SELF（仅本人）
 */
@SuppressWarnings("rawtypes")
public class DataScopeInnerInterceptor implements InnerInterceptor {

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter,
                            RowBounds rowBounds, ResultHandler resultHandler,
                            org.apache.ibatis.mapping.BoundSql boundSql) throws SQLException {
        InnerInterceptor.super.beforeQuery(executor, ms, parameter, rowBounds, resultHandler, boundSql);
    }

    /**
     * 根据当前用户 dataScope 构建过滤条件
     */
    public Expression buildDataScopeCondition() {
        UserContext ctx = UserContextHolder.get();
        if (ctx == null || isAdmin(ctx)) {
            return null;
        }

        String dataScope = ctx.getDataScope();
        if (dataScope == null || "0".equals(dataScope)) {
            return null;
        }

        Long userId = ctx.getUserId();
        Long deptId = ctx.getDeptId();

        return switch (dataScope) {
            case "1", "2", "3" -> deptId != null ? eq("dept_id", deptId) : null;
            case "4" -> userId != null ? eq("creator", userId) : null;
            default -> null;
        };
    }

    private Expression eq(String column, Long value) {
        EqualsTo eq = new EqualsTo();
        eq.setLeftExpression(new Column(column));
        eq.setRightExpression(new LongValue(value));
        return eq;
    }

    private boolean isAdmin(UserContext ctx) {
        return ctx.getRoles() != null && ctx.getRoles().contains("ROLE_ADMIN");
    }
}
