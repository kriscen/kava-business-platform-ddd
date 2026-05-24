package com.kava.kbpd.upms.api.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TenantStatusDTO implements Serializable {
    private String status;
    private Boolean expired;
}
