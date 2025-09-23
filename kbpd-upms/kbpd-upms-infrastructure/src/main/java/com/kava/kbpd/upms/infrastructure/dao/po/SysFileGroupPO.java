package com.kava.kbpd.upms.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kava.kbpd.common.database.po.TenantDeletablePO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 文件类型
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_file_group")
public class SysFileGroupPO extends TenantDeletablePO {

	private Long pid;

	private Long type;

	private String name;

}
