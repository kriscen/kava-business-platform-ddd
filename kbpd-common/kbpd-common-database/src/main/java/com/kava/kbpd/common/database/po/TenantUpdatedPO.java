package com.kava.kbpd.common.database.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author Kris
 * @date 2025/9/23
 * @description: 租户修改po
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class TenantUpdatedPO extends TenantCreatedPO {

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
}
