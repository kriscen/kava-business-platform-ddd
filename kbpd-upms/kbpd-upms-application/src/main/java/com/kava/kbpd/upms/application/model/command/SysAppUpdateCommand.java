package com.kava.kbpd.upms.application.model.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysAppUpdateCommand {
    private Long id;
    private String code;
    private String name;
    private String icon;
    private String description;
}
