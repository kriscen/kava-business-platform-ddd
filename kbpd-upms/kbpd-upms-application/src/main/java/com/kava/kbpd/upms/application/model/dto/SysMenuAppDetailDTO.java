package com.kava.kbpd.upms.application.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: Menu application detail
 */
@Data
public class SysMenuAppDetailDTO {

    private Long id;

    private String name;

    private String permission;

    private Long pid;

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

    private String creator;

    private LocalDateTime gmtCreate;

    private String modifier;

    private LocalDateTime gmtModified;
}
