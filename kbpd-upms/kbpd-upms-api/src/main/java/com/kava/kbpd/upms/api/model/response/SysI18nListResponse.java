package com.kava.kbpd.upms.api.model.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 国际化 响应对象
 */
@Data
public class SysI18nListResponse implements Serializable {

	/**
	 * id
	 */
	private Long id;

	/**
	 * key
	 */
	private String name;

	/**
	 * 中文
	 */
	private String zhCn;

	/**
	 * 英文
	 */
	private String en;

	/**
	 * 创建人
	 */
	private String creator;

	/**
	 * 创建时间
	 */
	private LocalDateTime gmtCreate;

	/**
	 * 修改人
	 */
	private String modifier;

	/**
	 * 更新时间
	 */
	private LocalDateTime gmtModified;

	/**
	 * 删除标记
	 */
	private String delFlag;

}
