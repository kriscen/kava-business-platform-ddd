package com.kava.kbpd.upms.api.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 客户端信息 request对象
 */
@Data
public class SysOauthClientDTO implements Serializable {



	/**
	 * 客户端ID
	 */
	private String clientId;

	/**
	 * 客户端密钥
	 */
	private String clientSecret;

	/**
	 * 资源ID
	 */
	private String resourceIds;

	/**
	 * 作用域
	 */
	private String scope;

	/**
	 * 授权方式[A,B,C]
	 */
	private String[] authorizedGrantTypes;

	/**
	 * 回调地址
	 */
	private String webServerRedirectUri;

	/**
	 * 权限
	 */
	private String authorities;

	/**
	 * 请求令牌有效时间
	 */
	private Integer accessTokenValidity;

	/**
	 * 刷新令牌有效时间
	 */
	private Integer refreshTokenValidity;

	/**
	 * 扩展信息
	 */
	private String additionalInformation;

	/**
	 * 是否自动放行
	 */
	private String autoapprove;

	/**
	 * 租户ID
	 */
	private Long tenantId;

	/**
	 * 用户类型
	 */
	private Integer userType;
}
