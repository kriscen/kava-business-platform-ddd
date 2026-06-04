package com.kava.kbpd.upms.api.model.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysAppListResponse implements Serializable {
    private Long id;
    private String code;
    private String name;
    private String icon;
    private String status;
}
