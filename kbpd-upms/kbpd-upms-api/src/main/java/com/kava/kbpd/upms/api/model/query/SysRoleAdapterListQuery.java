package com.kava.kbpd.upms.api.model.query;

import com.kava.kbpd.common.core.base.AdapterBaseListQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 角色 query对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRoleAdapterListQuery extends AdapterBaseListQuery {

    /** 角色名称 */
    private String roleName;

    /** 角色标识 */
    private String roleCode;

}
