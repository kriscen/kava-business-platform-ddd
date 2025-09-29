package com.kava.kbpd.upms.domain.model.entity;

import com.kava.kbpd.common.core.label.AggregateRoot;
import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.common.core.model.valobj.SysUserId;
import com.kava.kbpd.upms.domain.model.valobj.SysDeptId;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleId;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 用户表
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysUserEntity implements AggregateRoot<SysUserId> {

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
	private SysDeptId deptId;

	/**
	 * 租户ID
	 */
	private SysTenantId tenantId;

	/**
	 * 微信openid
	 */
	private String wxOpenid;

	/**
	 * 微信小程序openId
	 */
	private String miniOpenid;

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

	/**
	 * 所拥有的角色id集合
	 */
	private List<SysRoleId> roleIds;

	@Override
	public SysUserId identifier() {
		return id;
	}
}
