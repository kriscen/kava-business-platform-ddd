package com.kava.kbpd.member.api.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MemberInfoDTO implements Serializable {
    private Long id;
    private String mobile;
    private String password;
    private Long tenantId;
    private Long appId;
    private Boolean enabled;
}
