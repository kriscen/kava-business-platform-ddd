package com.kava.kbpd.upms.application.model.dto;

import lombok.Data;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: 用户应用层详情 DTO
 */
@Data
public class SysUserAppDetailDTO {
    private Long id;

    private String username;

    /**
     * 密码哈希值
     */
    private String password;

    private Long deptId;

    private Long tenantId;

    /**
     * 锁定标记："0"正常，"1"锁定
     */
    private String lockFlag;

    private String phone;

    private String nickname;

    private String name;

    private String email;

    private String avatar;
}
