package com.kava.kbpd.common.core.base;

import com.kava.kbpd.common.core.label.Entity;
import com.kava.kbpd.common.core.label.Identifier;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: cqrs模型下的 write接口
 */
public interface IBaseWriteRepository<I extends Identifier, E extends Entity<I>> {

    I create(E entity);

    Boolean update(E entity);

    Boolean removeBatchByIds(List<I> ids);
}
