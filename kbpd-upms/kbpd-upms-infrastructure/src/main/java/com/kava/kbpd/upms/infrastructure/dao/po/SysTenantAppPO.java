package com.kava.kbpd.upms.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kava.kbpd.common.database.po.SysCreatedPO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_tenant_app")
public class SysTenantAppPO extends SysCreatedPO {
    private Long tenantId;
    private Long appId;
    private String status;
    private LocalDateTime gmtModified;
}
