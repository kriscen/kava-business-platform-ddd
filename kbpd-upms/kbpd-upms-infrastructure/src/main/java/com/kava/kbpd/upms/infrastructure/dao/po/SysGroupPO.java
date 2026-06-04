package com.kava.kbpd.upms.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kava.kbpd.common.database.po.TenantDeletablePO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 分组管理
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_group")
public class SysGroupPO extends TenantDeletablePO {

	/**
	 * 分组名称
	 */
	private String name;

	/**
	 * 排序
	 */
	private Integer sortOrder;

	/**
	 * 父级分组id
	 */
	private Long pid;

}
