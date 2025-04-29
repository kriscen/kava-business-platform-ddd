package com.kava.kbpd.upms.api.model.query;

import com.kava.kbpd.common.core.base.BaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 客户端信息 query对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysOauthClientDetailsQuery extends BaseQuery {


	private Long id;

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
	 * 删除标记
	 */
	private String delFlag;

	/**
	 * 创建人
	 */
	private String creator;

	/**
	 * 创建时间
	 */
	private LocalDateTime gmtCreate;

	/**
	 * 修改人
	 */
	private String modifier;

	/**
	 * 更新时间
	 */
	private LocalDateTime gmtModified;

}
