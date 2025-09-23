package com.kava.kbpd.upms.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kava.kbpd.common.database.po.TenantDeletablePO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 部门管理
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_dept")
public class SysDeptPO extends TenantDeletablePO {

	/**
	 * 部门名称
	 */
	private String name;

	/**
	 * 排序
	 */
	private Integer sortOrder;

	/**
	 * 父级部门id
	 */
	private Long pid;

}
