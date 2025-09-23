package com.kava.kbpd.upms.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kava.kbpd.common.database.po.SysDeletablePO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 租户
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_tenant")
public class SysTenantPO extends SysDeletablePO {
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
	 * 0正常 9-冻结
	 */
	private String status;

	private String menuId;
}
