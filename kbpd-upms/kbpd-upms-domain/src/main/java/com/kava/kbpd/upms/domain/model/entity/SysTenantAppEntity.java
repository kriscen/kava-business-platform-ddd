package com.kava.kbpd.upms.domain.model.entity;

import com.kava.kbpd.common.core.label.Entity;
import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.domain.model.valobj.SysAppId;
import com.kava.kbpd.upms.domain.model.valobj.SysTenantAppId;
import com.kava.kbpd.upms.types.enums.SysTenantAppStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysTenantAppEntity implements Entity<SysTenantAppId> {

    private SysTenantAppId id;
    private SysTenantId tenantId;
    private SysAppId appId;
    private SysTenantAppStatus status;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    @Override
    public SysTenantAppId identifier() {
        return id;
    }
}
