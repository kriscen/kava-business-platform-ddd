package com.kava.kbpd.upms.application.model.dto;

import lombok.Data;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: group application detail
 */
@Data
public class SysGroupAppDetailDTO {
    private Long id;
    private String name;
    private Integer sortOrder;
    private Long pid;
    private String parentName;
}
