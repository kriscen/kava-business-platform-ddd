package com.kava.kbpd.upms.application.model.dto;

import com.kava.kbpd.upms.types.enums.SysTenantStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysTenantAppListDTO {
    private Long id;
    private String name;
    private String code;
    private SysTenantStatus status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime gmtCreate;
}
