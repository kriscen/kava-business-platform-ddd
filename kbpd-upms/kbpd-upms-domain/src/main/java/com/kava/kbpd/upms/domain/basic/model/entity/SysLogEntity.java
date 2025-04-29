package com.kava.kbpd.upms.domain.basic.model.entity;

import com.kava.kbpd.common.core.label.Entity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysLogId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 日志表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysLogEntity implements Entity<SysLogId> {

	/**
	 * 编号
	 */
	private SysLogId id;

	/**
	 * 日志类型
	 */
	private String logType;

	/**
	 * 日志标题
	 */
	private String title;

	/**
	 * 创建者
	 */
	private String createBy;

	/**
	 * 操作IP地址
	 */
	private String remoteAddr;

	/**
	 * 用户代理
	 */
	private String userAgent;

	/**
	 * 请求URI
	 */
	private String requestUri;

	/**
	 * 操作方式
	 */
	private String method;

	/**
	 * 操作提交的数据
	 */
	private String params;

	/**
	 * 执行时间
	 */
	private Long time;

	/**
	 * 异常信息
	 */
	private String exception;

	/**
	 * 服务ID
	 */
	private String serviceId;

	@Override
	public SysLogId identifier() {
		return id;
	}
}
