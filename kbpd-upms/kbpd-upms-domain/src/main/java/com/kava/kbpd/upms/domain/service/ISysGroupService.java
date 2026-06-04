package com.kava.kbpd.upms.domain.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.common.core.model.tree.Tree;
import com.kava.kbpd.upms.domain.model.entity.SysGroupEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysGroupId;
import com.kava.kbpd.upms.domain.model.valobj.SysGroupListQuery;

import java.util.List;

public interface ISysGroupService {
    SysGroupId create(SysGroupEntity entity);

    Boolean update(SysGroupEntity entity);

    PagingInfo<SysGroupEntity> queryPage(SysGroupListQuery query);

    SysGroupEntity queryById(SysGroupId id);

    Boolean removeBatchByIds(List<SysGroupId> ids);

    List<SysGroupEntity> queryAll();

    List<Tree<Long>> queryTree();
}
