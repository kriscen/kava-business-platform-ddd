package com.kava.kbpd.upms.domain.basic.service;

import cn.hutool.core.lang.tree.Tree;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.basic.model.entity.SysAreaEntity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysAreaId;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysAreaListQuery;

import java.util.List;

/**
 * @author Kris
 * @date 2025/3/26
 * @description: 行政区划 service
 */
public interface ISysAreaService {

    SysAreaId create(SysAreaEntity sysAreaEntity);

    Boolean update(SysAreaEntity sysAreaEntity);

    PagingInfo<SysAreaEntity> queryPage(SysAreaListQuery query);

    SysAreaEntity queryById(SysAreaId id);

    List<Tree<Long>> selectTree(SysAreaListQuery query);

    Boolean removeBatchByIds(List<SysAreaId> ids);
}
