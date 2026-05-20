package com.kava.kbpd.upms.domain.model.valobj;

import com.kava.kbpd.common.core.base.QueryParamValObj;
import com.kava.kbpd.common.core.label.ValueObject;
import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 角色
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
public class SysRoleListQuery implements ValueObject {

    QueryParamValObj queryParam;
    String roleName;
    String roleCode;
    SysTenantId tenantId;

}
