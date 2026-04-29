package com.kava.kbpd.common.security.context;

import com.kava.kbpd.common.core.model.UserContext;

/**
 * 基于 ThreadLocal 的 UserContext 持有者
 */
public class UserContextHolder {

    private static final ThreadLocal<UserContext> CONTEXT = new ThreadLocal<>();

    public static UserContext get() {
        return CONTEXT.get();
    }

    public static void set(UserContext context) {
        CONTEXT.set(context);
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
