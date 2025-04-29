package com.kava.kbpd.upms.domain.basic.model.valobj;

import com.kava.kbpd.common.core.label.Identifier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 租户 id
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
public class SysTenantId implements Identifier {

	/**
	 * 租户id
	 */
	Long id;
}
