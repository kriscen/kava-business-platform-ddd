package com.kava.kbpd.upms.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 租户
 */
@Data
@TableName("sys_tenant")
public class SysTenantPO implements Serializable {

	/**
	 * 租户id
	 */
	@TableId
	private Long id;

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

	/**
	 * 创建人
	 */
	@TableField(fill = FieldFill.INSERT)
	private String creator;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime gmtCreate;

	/**
	 * 修改人
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private String modifier;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime gmtModified;

	/**
	 * 删除标记
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	private String delFlag;
}
