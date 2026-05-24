package com.kava.kbpd.upms.api.model.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysTenantDropdownResponse implements Serializable {

    private Long id;

    private String name;

    private String code;

    private String status;
}
