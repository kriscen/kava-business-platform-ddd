package com.kava.kbpd.upms.application.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: Menu list query result
 */
@Data
public class SysMenuAppListDTO {

    private Long id;

    private String name;

    private String permission;

    private Long parentId;

    private String icon;

    private String path;

    private String component;

    private String visible;

    private Integer sortOrder;

    private String menuType;

    private String keepAlive;

    private String embedded;

    private String scope;

    private Long tenantId;

    private String parentName;

    /**
     * 子菜单列表（用于树结构）
     */
    private List<SysMenuAppListDTO> children;
}
