package com.kava.kbpd.common.core.model.valobj;

import com.kava.kbpd.common.core.label.Identifier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: b端用户 id
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
public class SysUserId implements Identifier {

	/**
	 * 主键ID
	 */
	Long id;

	public static SysUserId of(Long id) {
		return id == null ? null : builder().id(id).build();
	}
}
