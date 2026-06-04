package com.kava.kbpd.upms.api.model.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SysTenantAppListResponse implements Serializable {
    private Long id;
    private Long tenantId;
    private Long appId;
    private String appCode;
    private String appName;
    private String appIcon;
    private String status;
    private LocalDateTime gmtCreate;
}
