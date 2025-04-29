package com.kava.kbpd.upms.domain.permission.model.valobj;

import com.kava.kbpd.common.core.label.Identifier;
import lombok.*;

import static lombok.AccessLevel.PRIVATE;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 菜单权限 id
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
public class SysMenuId implements Identifier {

    /**
     * 菜单ID
     */
    Long id;

}
