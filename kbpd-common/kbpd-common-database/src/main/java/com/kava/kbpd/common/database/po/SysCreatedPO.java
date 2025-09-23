package com.kava.kbpd.common.database.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author Kris
 * @date 2025/9/23
 * @description: 系统创建po
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class SysCreatedPO extends BasePO {
    /**
     * 主键ID
     */
    @TableId
    private Long id;

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
}
