package com.kava.kbpd.upms.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 公共参数配置
 */
@Data
@TableName("sys_public_param")
public class SysPublicParamPO implements Serializable {

	/**
	 * 编号
	 */
	@TableId
	private Long id;

	/**
	 * 公共参数名称
	 */
	private String publicName;

	/**
	 * 公共参数地址值,英文大写+下划线
	 */
	private String publicKey;

	/**
	 * 值
	 */
	private String publicValue;

	/**
	 * 状态（1有效；2无效；）
	 */
	private String status;

	/**
	 * 公共参数编码
	 */
	private String validateCode;

	/**
	 * 是否是系统内置
	 */
	private String systemFlag;

	/**
	 * 配置类型：0-默认；1-检索；2-原文；3-报表；4-安全；5-文档；6-消息；9-其他
	 */
	private String publicType;

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
