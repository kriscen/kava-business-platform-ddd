package com.kava.kbpd.auth.constants;

/**
 * @author Kris
 * @date 2025/4/1
 * @description: SecurityConstants
 */
public interface AuthConstants {

	/**
	 * http 请求头 用户类型
	 */
	String HTTP_HEARD_USER_TYPE = "user_type";

	/**
	 * jwk 缓存 key
	 */
	String JWK_SET_KEY = "jwk";

	/**
	 * url 参数 用户类型
	 */
	String URL_PARAM_USER_TYPE = "userType";

	/**
	 * url 参数 租户id
	 */
	String URL_PARAM_TENANT_ID = "tenantId";

	/**
	 * url 登录
	 */
	String URL_OAUTH2_LOGIN = "/oauth2/login";

	/**
	 * url 错误
	 */
	String URL_OAUTH2_ERROR = "/oauth2/error";

	String URL_OAUTH2_CONSENT = "/oauth2/consent";

	String ATTR_STATE = "state";

}
