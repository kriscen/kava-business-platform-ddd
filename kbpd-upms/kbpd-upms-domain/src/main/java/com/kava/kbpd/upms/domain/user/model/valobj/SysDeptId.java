package com.kava.kbpd.upms.domain.user.model.valobj;

import com.kava.kbpd.common.core.label.Identifier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 部门 id
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
public class SysDeptId implements Identifier {

	Long id;
}
