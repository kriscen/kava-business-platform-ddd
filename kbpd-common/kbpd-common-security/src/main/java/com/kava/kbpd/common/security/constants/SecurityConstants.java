package com.kava.kbpd.common.security.constants;

/**
 * @author Kris
 * @date 2025/4/1
 * @description: SecurityConstants
 */
public interface SecurityConstants {

	/**
	 * {bcrypt} 加密的特征码
	 */
	String BCRYPT = "{bcrypt}";

	/**
	 * {noop} 加密的特征码
	 */
	String NOOP = "{noop}";

	/**
	 * http 请求头 用户类型
	 */
	String HTTP_HEARD_USER_TYPE = "user_type";

	/**
	 * jwt 用户类型
	 */
	String JWT_USER_TYPE = "userType";

	/**
	 * 登录页
	 */
	String OAUTH2_VIEW_LOGIN_URL = "login.html";

	/**
	 * 授权页面
	 */
	String OAUTH2_VIEW_CONFIRM_URL = "confirm.html";

	/**
	 * 短信验证码
	 */
	String PARAM_SMS_CODE = "smsCode";

	/**
	 * 手机号
	 */
	String PARAM_MOBILE = "mobile";
}
