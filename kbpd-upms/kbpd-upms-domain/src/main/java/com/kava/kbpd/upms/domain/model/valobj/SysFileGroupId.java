package com.kava.kbpd.upms.domain.model.valobj;

import com.kava.kbpd.common.core.label.Identifier;
import lombok.*;

import static lombok.AccessLevel.PRIVATE;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 文件类型 id
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
public class SysFileGroupId implements Identifier {

	Long id;

	public static SysFileGroupId of(Long id) {
		return id == null ? null : builder().id(id).build();
	}
}
