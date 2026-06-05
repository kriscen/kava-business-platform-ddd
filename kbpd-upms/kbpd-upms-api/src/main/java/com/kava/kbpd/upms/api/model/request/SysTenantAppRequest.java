package com.kava.kbpd.upms.api.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysTenantAppRequest implements Serializable {
    private Long appId;
}
