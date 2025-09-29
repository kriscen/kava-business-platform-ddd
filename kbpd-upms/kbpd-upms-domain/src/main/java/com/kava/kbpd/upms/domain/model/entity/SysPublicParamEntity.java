package com.kava.kbpd.upms.domain.model.entity;

import com.kava.kbpd.common.core.label.Entity;
import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.domain.model.valobj.SysPublicParamId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 公共参数配置
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysPublicParamEntity implements Entity<SysPublicParamId> {

	/**
	 * 编号
	 */
	private SysPublicParamId id;

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
	 * 租户ID
	 */
	private SysTenantId tenantId;

	@Override
	public SysPublicParamId identifier() {
		return id;
	}
}
