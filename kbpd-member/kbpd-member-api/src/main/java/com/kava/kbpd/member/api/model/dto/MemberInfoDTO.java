package com.kava.kbpd.member.api.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Kris
 * @date 2025/4/2
 * @description:
 */
@Data
public class MemberInfoDTO implements Serializable {
    private Long id;

    /**
     * 权限标识集合
     */
    private List<String> permissions;

    /**
     * 角色集合
     */
    private List<String> roles;
}
