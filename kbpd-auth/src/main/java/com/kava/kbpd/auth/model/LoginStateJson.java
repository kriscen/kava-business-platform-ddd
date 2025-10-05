package com.kava.kbpd.auth.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Kris
 * @date 2025/10/5
 * @description: oauth2 login state json格式
 */
@Data
public class LoginStateJson implements Serializable {

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 用户类型
     */
    private Integer userType;
}
