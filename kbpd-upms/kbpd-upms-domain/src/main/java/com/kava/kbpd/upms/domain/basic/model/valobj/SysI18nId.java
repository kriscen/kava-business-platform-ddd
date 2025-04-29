package com.kava.kbpd.upms.domain.basic.model.valobj;

import com.kava.kbpd.common.core.label.Identifier;
import lombok.*;

import static lombok.AccessLevel.PRIVATE;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 系统表-国际化 id
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
public class SysI18nId implements Identifier {

	/**
	 * id
	 */
	Long id;
}
