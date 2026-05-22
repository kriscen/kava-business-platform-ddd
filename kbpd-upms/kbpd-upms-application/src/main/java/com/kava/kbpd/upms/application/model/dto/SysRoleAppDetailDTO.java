package com.kava.kbpd.upms.application.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: role application detail
 */
@Data
public class SysRoleAppDetailDTO {

    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色标识
     */
    private String roleCode;

    /**
     * 角色描述
     */
    private String roleDesc;

    /**
     * 数据权限类型
     */
    private String dsType;

    /**
     * 数据权限作用范围
     */
    private String dsScope;

    /**
     * 关联的菜单ID列表
     */
    private List<Long> menuIds;

    private Long tenantId;
}
