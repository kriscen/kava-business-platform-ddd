package com.kava.kbpd.upms.domain.repository;

import com.kava.kbpd.common.core.base.IBaseSimpleRepository;
import com.kava.kbpd.upms.domain.model.entity.SysDeptEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysDeptId;
import com.kava.kbpd.upms.domain.model.valobj.SysDeptListQuery;

import java.util.List;

public interface ISysDeptRepository extends IBaseSimpleRepository<SysDeptId, SysDeptEntity, SysDeptListQuery> {

    List<SysDeptEntity> queryAll();

    List<SysDeptEntity> queryByPid(SysDeptId pid);

    boolean existsUserReference(SysDeptId deptId);
}