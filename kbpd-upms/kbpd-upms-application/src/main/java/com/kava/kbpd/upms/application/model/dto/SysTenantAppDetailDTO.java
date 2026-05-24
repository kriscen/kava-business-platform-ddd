package com.kava.kbpd.upms.application.model.dto;

import com.kava.kbpd.upms.types.enums.SysTenantStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysTenantAppDetailDTO {
    private Long id;
    private String name;
    private String code;
    private String tenantDomain;
    private String websiteName;
    private String logo;
    private String footer;
    private String miniQr;
    private String background;
    private SysTenantStatus status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String menuId;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
