package com.kava.kbpd.upms.application.model.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: user create command
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUserCreateCommand {

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
     * 关联的角色ID列表
     */
    private List<Long> roleIds;
}
