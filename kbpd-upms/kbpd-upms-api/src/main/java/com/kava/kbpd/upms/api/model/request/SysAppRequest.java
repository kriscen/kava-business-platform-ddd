package com.kava.kbpd.upms.api.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SysAppRequest implements Serializable {
    private Long id;
    private String code;
    private String name;
    private String icon;
    private String description;
    private List<Long> menuIds;
}
