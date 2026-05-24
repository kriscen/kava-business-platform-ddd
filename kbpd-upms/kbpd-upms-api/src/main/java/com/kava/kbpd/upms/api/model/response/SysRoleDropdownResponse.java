package com.kava.kbpd.upms.api.model.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysRoleDropdownResponse implements Serializable {

    private Long id;

    private String roleName;

    private String roleCode;
}
