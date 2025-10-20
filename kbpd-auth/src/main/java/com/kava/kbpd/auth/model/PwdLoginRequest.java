package com.kava.kbpd.auth.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Kris
 * @date 2025/10/13
 * @description: pwd login req
 */
@Data
public class PwdLoginRequest implements Serializable {
    private String username;
    private String password;
    private String tenantId;
    private Integer userType;
}
