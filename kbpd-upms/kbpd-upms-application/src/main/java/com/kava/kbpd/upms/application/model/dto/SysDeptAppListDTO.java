package com.kava.kbpd.upms.application.model.dto;

import lombok.Data;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: dept list query result
 */
@Data
public class SysDeptAppListDTO {
    private Long id;
    private String name;
    private Integer sortOrder;
    private Long pid;
    private String parentName;
    private java.util.List<SysDeptAppListDTO> children;
}
