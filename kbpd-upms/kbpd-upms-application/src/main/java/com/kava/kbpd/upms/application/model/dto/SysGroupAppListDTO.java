package com.kava.kbpd.upms.application.model.dto;

import lombok.Data;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: group list query result
 */
@Data
public class SysGroupAppListDTO {
    private Long id;
    private String name;
    private Integer sortOrder;
    private Long pid;
    private String parentName;
    private java.util.List<SysGroupAppListDTO> children;
}
