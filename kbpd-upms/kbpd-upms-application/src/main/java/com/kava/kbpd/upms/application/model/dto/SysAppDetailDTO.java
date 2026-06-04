package com.kava.kbpd.upms.application.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysAppDetailDTO {
    private Long id;
    private String code;
    private String name;
    private String icon;
    private String description;
    private String status;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
