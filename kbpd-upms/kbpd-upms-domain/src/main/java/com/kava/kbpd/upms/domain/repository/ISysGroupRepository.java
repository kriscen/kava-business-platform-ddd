package com.kava.kbpd.upms.domain.repository;

import com.kava.kbpd.common.core.base.IBaseSimpleRepository;
import com.kava.kbpd.upms.domain.model.entity.SysGroupEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysGroupId;
import com.kava.kbpd.upms.domain.model.valobj.SysGroupListQuery;

import java.util.List;

public interface ISysGroupRepository extends IBaseSimpleRepository<SysGroupId, SysGroupEntity, SysGroupListQuery> {

    List<SysGroupEntity> queryAll();

    List<SysGroupEntity> queryByIds(List<SysGroupId> ids);

    List<SysGroupEntity> queryByPid(SysGroupId pid);

    boolean existsUserReference(SysGroupId groupId);
}
