package com.kava.kbpd.upms.application.model.command;

import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysTenantUpdateCommand {
    private SysTenantId id;
    private String name;
    private String code;
    private String tenantDomain;
    private String websiteName;
    private String logo;
    private String footer;
    private String miniQr;
    private String background;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String menuId;
}
