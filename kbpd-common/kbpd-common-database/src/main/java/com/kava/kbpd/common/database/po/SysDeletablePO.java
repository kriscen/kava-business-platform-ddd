package com.kava.kbpd.common.database.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Kris
 * @date 2025/9/23
 * @description: 系统软删除po
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class SysDeletablePO extends SysUpdatedPO {

    /**
     * 删除标记
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private String delFlag;
}
