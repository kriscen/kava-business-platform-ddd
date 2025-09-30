package com.kava.kbpd.upms.api.model.query;

import com.kava.kbpd.common.core.base.AdapterBaseListQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 文件类型 query对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRouteConfAdapterListQuery extends AdapterBaseListQuery {


	private Long id;

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

	/**
	 * 创建时间
	 */
	private LocalDateTime gmtCreate;

	/**
	 * 更新时间
	 */
	private LocalDateTime gmtModified;

	/**
	 * 删除标识（0-正常,1-删除）
	 */
	private String delFlag;

}
