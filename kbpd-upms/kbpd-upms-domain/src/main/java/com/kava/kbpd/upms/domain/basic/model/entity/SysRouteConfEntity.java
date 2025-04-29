package com.kava.kbpd.upms.domain.basic.model.entity;

import com.kava.kbpd.common.core.label.Entity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysRouteConfId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 路由配置
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysRouteConfEntity implements Entity<SysRouteConfId> {


	private SysRouteConfId id;

	/**
	 * 路由ID
	 */
	private String routeId;

	/**
	 * 路由名称
	 */
	private String routeName;

	/**
	 * 断言
	 */
	private String predicates;

	/**
	 * 过滤器
	 */
	private String filters;

	/**
	 * uri
	 */
	private String uri;

	/**
	 * 排序
	 */
	private Integer sortOrder;

	private String metadata;

	@Override
	public SysRouteConfId identifier() {
		return id;
	}
}
