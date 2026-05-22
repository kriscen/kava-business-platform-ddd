package com.kava.kbpd.upms.api.model.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 角色 列表响应对象
 */
@Data
public class SysRoleListResponse implements Serializable {

    private Long id;

    /** 角色名称 */
    private String roleName;

    /** 角色标识 */
    private String roleCode;

    /** 角色描述 */
    private String roleDesc;

    /** 数据权限类型 */
    private String dsType;

    /** 数据权限作用范围 */
    private String dsScope;

    /** 创建时间 */
    private LocalDateTime gmtCreate;

    /** 更新时间 */
    private LocalDateTime gmtModified;

}
