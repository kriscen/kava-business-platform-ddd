package com.kava.kbpd.upms.domain.model.valobj;

import com.kava.kbpd.common.core.label.Identifier;
import lombok.*;

import static lombok.AccessLevel.PRIVATE;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 公共参数配置 id
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
public class SysPublicParamId implements Identifier {

	/**
	 * 编号
	 */
	Long id;
}
