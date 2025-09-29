package com.kava.kbpd.common.core.base;

import com.kava.kbpd.common.core.label.Entity;
import com.kava.kbpd.common.core.label.Identifier;
import com.kava.kbpd.common.core.label.ValueObject;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: repository基本接口 - query
 */
public interface IBaseQueryRepository<I extends Identifier, E extends Entity<I>, Q extends ValueObject> {

    PagingInfo<E> queryPage(Q query);

    E queryById(I id);

}
