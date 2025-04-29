package com.kava.kbpd.upms.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 菜单权限表
 */
@Data
@TableName("sys_menu")
public class SysMenuPO implements Serializable {

    /**
     * 菜单ID
     */
    @TableId
    private Long id;

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
    private Long parentId;

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
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String modifier;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    /**
     * 0--正常 1--删除
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private String delFlag;

}
