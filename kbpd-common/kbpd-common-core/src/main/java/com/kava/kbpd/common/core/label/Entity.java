package com.kava.kbpd.common.core.label;

import java.io.Serializable;

/**
 * @author Kris
 * @date 2025/3/19
 * @description: 实体
 */
public interface Entity<T extends Identifier> extends Serializable {
    /**
     * 返回唯一标识
     */
    T identifier();
}
