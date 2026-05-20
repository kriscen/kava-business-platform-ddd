package com.kava.kbpd.upms.application.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: role query list result
 */
@Data
public class SysRoleAppListDTO {

    private Long id;

    /** 角色名称 */
    private String roleName;

    /** 角色标识 */
    private String roleCode;

    /** 角色描述 */
    private String roleDesc;

    /** 数据权限类型 */
    private Integer dsType;

    /** 数据权限作用范围 */
    private String dsScope;

    /** 创建时间 */
    private LocalDateTime gmtCreate;

    /** 更新时间 */
    private LocalDateTime gmtModified;

}
