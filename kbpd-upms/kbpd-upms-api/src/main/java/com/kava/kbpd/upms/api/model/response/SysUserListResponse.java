package com.kava.kbpd.upms.api.model.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 用户 列表响应对象
 */
@Data
public class SysUserListResponse implements Serializable {

	/**
	 * 主键ID
	 */
	private Long id;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;


	/**
	 * 创建人
	 */
	private String creator;

	/**
	 * 创建时间
	 */
	private LocalDateTime gmtCreate;

	/**
	 * 修改人
	 */
	private String modifier;

	/**
	 * 更新时间
	 */
	private LocalDateTime gmtModified;

	/**
	 * 0-正常，1-删除
	 */
	private String delFlag;

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

}
