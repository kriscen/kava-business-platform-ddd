package com.kava.kbpd.upms.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.kava.kbpd.common.core.model.valobj.SysUserId;
import com.kava.kbpd.upms.domain.model.aggregate.SysUserEntity;
import com.kava.kbpd.upms.domain.repository.ISysUserWriteRepository;
import com.kava.kbpd.upms.infrastructure.converter.SysUserConverter;
import com.kava.kbpd.upms.infrastructure.dao.SysUserMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysUserPO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysUserWriteRepository implements ISysUserWriteRepository {

    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysUserConverter sysUserConverter;

    @Override
    public SysUserId create(SysUserEntity entity) {
        SysUserPO sysUserPO = sysUserConverter.convertEntity2PO(entity);
        sysUserMapper.insert(sysUserPO);
        return SysUserId.builder()
                .id(sysUserPO.getId())
                .build();
    }

    @Override
    public Boolean update(SysUserEntity entity) {
        SysUserPO sysUserPO = sysUserConverter.convertEntity2PO(entity);
        return SqlHelper.retBool(sysUserMapper.updateById(sysUserPO));
    }

    @Override
    public Boolean removeBatchByIds(List<SysUserId> ids) {
        List<Long> idList = ids.stream().map(SysUserId::getId).toList();
        return SqlHelper.retBool(sysUserMapper.deleteByIds(idList));
    }
}