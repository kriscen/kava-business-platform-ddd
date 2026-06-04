package com.kava.kbpd.upms.application.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TenantAppListDTO {
    private Long id;
    private Long tenantId;
    private Long appId;
    private String appCode;
    private String appName;
    private String appIcon;
    private String status;
    private LocalDateTime gmtCreate;
}
