package com.kava.kbpd.upms.application.model.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: dept update command
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysDeptUpdateCommand {
    private Long id;
    private String name;
    private Integer sortOrder;
    private Long pid;
}
