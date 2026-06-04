package com.kava.kbpd.upms.application.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: user list query result
 */
@Data
public class SysUserAppListDTO {
    private Long id;
    private String username;
    private String lockFlag;
    private String phone;
    private String nickname;
    private String name;
    private String email;
    private String avatar;
    private Long groupId;
    private Long tenantId;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    private String groupName;
    private String tenantName;
    private List<Long> roleIds;
}
