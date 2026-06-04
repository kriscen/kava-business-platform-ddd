package com.kava.kbpd.upms.application.model.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: Menu update command
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysMenuUpdateCommand {

    private Long id;

    private String name;

    private String permission;

    private Long pid;

    private String icon;

    private String path;

    private String component;

    private String visible;

    private Integer sortOrder;

    private String menuType;

    private String keepAlive;

    private String embedded;

    private String level;
}
