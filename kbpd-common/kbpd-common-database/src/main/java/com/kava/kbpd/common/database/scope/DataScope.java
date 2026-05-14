package com.kava.kbpd.common.database.scope;

import java.lang.annotation.*;

/**
 * 数据权限注解：标记需要行级过滤的查询方法
 * 根据 userContext.dataScope（dsType）自动拼接 SQL 条件
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {

    /**
     * 部门表别名，默认 "dept"
     */
    String deptAlias() default "dept";

    /**
     * 用户表别名，默认 "u"
     */
    String userAlias() default "u";
}
