package com.kava.kbpd.upms.application.model.dto;

import lombok.Data;

@Data
public class TenantStatusAppDTO {
    private String status;
    private Boolean expired;
}
