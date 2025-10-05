package com.kava.kbpd.auth.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Kris
 * @date 2025/10/5
 * @description: oauth2 login 三方传递的参数
 */
@Data
public class Oauth2LoginContext implements Serializable {

    /**
     * 标准扩展字段，json的base url 格式
     */
    private String state;
}
