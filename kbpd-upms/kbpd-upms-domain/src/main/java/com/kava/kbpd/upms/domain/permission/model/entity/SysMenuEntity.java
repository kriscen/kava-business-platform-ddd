package com.kava.kbpd.upms.domain.permission.model.entity;

import com.kava.kbpd.common.core.label.Entity;
import com.kava.kbpd.upms.domain.permission.model.valobj.SysMenuId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 菜单权限 实体对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysMenuEntity implements Entity<SysMenuId> {

    /**
     * 菜单ID
     */
    private SysMenuId id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单权限标识
     */
    private String permission;

    /**
     * 父菜单ID
     */
    private SysMenuId pid;

    /**
     * 图标
     */
    private String icon;

    /**
     * 前端路由标识路径，默认和 comment 保持一致 过期
     */
    private String path;

    /**
     * 前端组件
     */
    private String component;

    /**
     * 菜单显示隐藏控制
     */
    private String visible;

    /**
     * 排序值
     */
    private Integer sortOrder;

    /**
     * 菜单类型 （0菜单 1按钮）
     */
    private String menuType;

    /**
     * 路由缓冲
     */
    private String keepAlive;

    /**
     * 菜单是否内嵌
     */
    private String embedded;

    /**
     * 菜单范围，区分平台还是租户
     */
    private String scope;

    @Override
    public SysMenuId identifier() {
        return id;
    }
}
