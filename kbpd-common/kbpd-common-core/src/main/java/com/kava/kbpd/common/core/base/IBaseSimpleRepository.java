package com.kava.kbpd.common.core.base;

import com.kava.kbpd.common.core.label.Entity;
import com.kava.kbpd.common.core.label.Identifier;
import com.kava.kbpd.common.core.label.ValueObject;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: repository基本接口 - 不使用cqrs思想，适用于比较薄的repository
 */
public interface IBaseSimpleRepository<I extends Identifier, E extends Entity<I>, Q extends ValueObject> {
    I create(E entity);

    Boolean update(E entity);

    Boolean removeBatchByIds(List<I> ids);

    PagingInfo<E> queryPage(Q query);

    E queryById(I id);
}
