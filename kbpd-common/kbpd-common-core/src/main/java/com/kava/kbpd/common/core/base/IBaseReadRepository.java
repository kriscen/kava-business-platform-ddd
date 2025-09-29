package com.kava.kbpd.common.core.base;

import com.kava.kbpd.common.core.label.Entity;
import com.kava.kbpd.common.core.label.Identifier;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: cqrs模型下的 read接口
 */
public interface IBaseReadRepository<I extends Identifier, E extends Entity<I>> {

    E queryById(I id);

}
