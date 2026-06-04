package com.kava.kbpd.upms.api.model.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SysAppDetailResponse implements Serializable {
    private Long id;
    private String code;
    private String name;
    private String icon;
    private String description;
    private String status;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
