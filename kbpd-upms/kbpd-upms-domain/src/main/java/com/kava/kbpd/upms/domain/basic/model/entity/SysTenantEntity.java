package com.kava.kbpd.upms.domain.basic.model.entity;

import com.kava.kbpd.common.core.label.Entity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysTenantId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 租户
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysTenantEntity implements Entity<SysTenantId> {

	/**
	 * 租户id
	 */
	private SysTenantId id;

	/**
	 * 租户名称
	 */
	private String name;

	/**
	 * 租户编号
	 */
	private String code;

	/**
	 * 租户域名
	 */
	private String tenantDomain;

	/**
	 * 网站名称
	 */
	private String websiteName;

	/**
	 * logo
	 */
	private String logo;

	/**
	 * footer
	 */
	private String footer;

	/**
	 * 移动端二维码
	 */
	private String miniQr;

	/**
	 * 登录页图片
	 */
	private String background;

	/**
	 * 开始时间
	 */
	private LocalDateTime startTime;

	/**
	 * 结束时间
	 */
	private LocalDateTime endTime;

	/**
	 * 0正常 9-冻结
	 */
	private String status;

	private String menuId;

	@Override
	public SysTenantId identifier() {
		return id;
	}
}
