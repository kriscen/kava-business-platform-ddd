package com.kava.kbpd.upms.application.model.dto;

import lombok.Data;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: dept application detail
 */
@Data
public class SysDeptAppDetailDTO {
    private Long id;
    private String name;
    private Integer sortOrder;
    private Long pid;
}
