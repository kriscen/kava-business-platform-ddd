package com.kava.kbpd.upms.domain.user.model.entity;

import com.kava.kbpd.common.core.label.Entity;
import com.kava.kbpd.upms.domain.user.model.valobj.SysUserId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 用户表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysUserEntity implements Entity<SysUserId> {

	/**
	 * 主键ID
	 */
	private SysUserId id;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 锁定标记
	 */
	private String lockFlag;

	/**
	 * 密码过期标记
	 */
	private String passwordExpireFlag;

	/**
	 * 密码修改时间
	 */
	private LocalDateTime passwordModifyTime;

	/**
	 * 手机号
	 */
	private String phone;

	/**
	 * 头像
	 */
	private String avatar;

	/**
	 * 部门ID
	 */
	private Long deptId;

	/**
	 * 租户ID
	 */
	private Long tenantId;

	/**
	 * 微信openid
	 */
	private String wxOpenid;

	/**
	 * 企微微信 userid
	 */
	private String wxCpUserid;

	/**
	 * 钉钉 userid
	 */
	private String wxDingUserid;

	/**
	 * 微信小程序openId
	 */
	private String miniOpenid;

	/**
	 * QQ openid
	 */
	private String qqOpenid;

	/**
	 * 昵称
	 */
	private String nickname;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 邮箱
	 */
	private String email;

	@Override
	public SysUserId identifier() {
		return id;
	}
}
