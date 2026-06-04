package com.kava.kbpd.upms.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kava.kbpd.common.database.po.SysDeletablePO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_app")
public class SysAppPO extends SysDeletablePO {
    private String code;
    private String name;
    private String icon;
    private String description;
    private String status;
}
