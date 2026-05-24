package com.kava.kbpd.upms.domain.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.common.core.model.tree.Tree;
import com.kava.kbpd.upms.domain.model.entity.SysAreaEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysAreaId;
import com.kava.kbpd.upms.domain.model.valobj.SysAreaListQuery;

import java.util.List;

public interface ISysAreaService {

    SysAreaId create(SysAreaEntity entity);

    Boolean update(SysAreaEntity entity);

    Boolean removeBatchByIds(List<SysAreaId> ids);

    PagingInfo<SysAreaEntity> queryPage(SysAreaListQuery query);

    SysAreaEntity queryById(SysAreaId id);

    List<Tree<Long>> selectAreaTree(SysAreaListQuery query);

    List<SysAreaEntity> selectChildren(SysAreaId pid);

}
