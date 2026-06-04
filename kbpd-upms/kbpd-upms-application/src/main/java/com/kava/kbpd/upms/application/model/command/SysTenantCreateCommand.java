package com.kava.kbpd.upms.application.model.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysTenantCreateCommand {
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
    private String adminUsername;
    private String adminPassword;
}
