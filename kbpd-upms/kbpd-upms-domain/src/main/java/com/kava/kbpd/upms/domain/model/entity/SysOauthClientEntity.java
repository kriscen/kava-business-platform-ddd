package com.kava.kbpd.upms.domain.model.entity;

import com.kava.kbpd.common.core.label.Entity;
import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.domain.model.valobj.SysOauthClientId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 客户端信息 实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysOauthClientEntity implements Entity<SysOauthClientId> {


	private SysOauthClientId id;

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
	private SysTenantId tenantId;

	/**
	 * 对应user场景
	 * tob or toc
	 */
	private Integer userType;

	@Override
	public SysOauthClientId identifier() {
		return id;
	}
}
