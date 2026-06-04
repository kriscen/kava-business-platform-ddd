package com.kava.kbpd.common.core.constants;

public interface JwtClaimConstants {

    /**
     * 用户ID
     */
    String USER_ID = "userId";

    /**
     * 用户名
     */
    String USERNAME = "username";

    /**
     * 分组ID
     */
    String GROUP_ID = "groupId";

    /**
     * 数据权限
     */
    String DATA_SCOPE = "dataScope";

    /**
     * 权限(角色Code)集合
     */
    String AUTHORITIES = "authorities";

    /**
     * 会员ID
     */
    String MEMBER_ID = "memberId";

    /**
     * 租户ID
     */
    String TENANT_ID = "tenantId";

    /**
     * 用户类型 ("1"=B端, "2"=C端)
     */
    String USER_TYPE = "userType";

    /**
     * 角色代码集合
     */
    String ROLES = "roles";
}
