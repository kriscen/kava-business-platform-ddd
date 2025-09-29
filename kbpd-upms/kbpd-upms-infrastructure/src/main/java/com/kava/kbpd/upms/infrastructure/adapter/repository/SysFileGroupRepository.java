package com.kava.kbpd.upms.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysFileGroupEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysFileGroupId;
import com.kava.kbpd.upms.domain.model.valobj.SysFileGroupListQuery;
import com.kava.kbpd.upms.domain.repository.ISysFileGroupRepository;
import com.kava.kbpd.upms.infrastructure.converter.SysFileGroupConverter;
import com.kava.kbpd.upms.infrastructure.dao.SysFileGroupMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysFileGroupPO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysFileGroupRepository implements ISysFileGroupRepository {

    @Resource
    private SysFileGroupMapper sysFileGroupMapper;
    @Resource
    private SysFileGroupConverter sysFileGroupConverter;
    @Override
    public SysFileGroupId create(SysFileGroupEntity entity) {
        SysFileGroupPO sysFileGroupPO = sysFileGroupConverter.convertEntity2PO(entity);
        sysFileGroupMapper.insert(sysFileGroupPO);
        return SysFileGroupId.builder()
                .id(sysFileGroupPO.getId())
                .build();
    }

    @Override
    public Boolean update(SysFileGroupEntity entity) {
        SysFileGroupPO sysFileGroupPO = sysFileGroupConverter.convertEntity2PO(entity);
        return SqlHelper.retBool(sysFileGroupMapper.updateById(sysFileGroupPO));
    }

    @Override
    public PagingInfo<SysFileGroupEntity> queryPage(SysFileGroupListQuery query) {
        Page<SysFileGroupPO> sysFileGroupPOPage = sysFileGroupMapper.selectPage(
                Page.of(query.getQueryParam().getPageNo(), query.getQueryParam().getPageSize()),
                Wrappers.lambdaQuery(SysFileGroupPO.class));
        return PagingInfo.toResponse(sysFileGroupPOPage.getRecords().stream()
                        .map(sysFileGroupConverter::convertPO2Entity).toList(),
                sysFileGroupPOPage.getTotal(), sysFileGroupPOPage.getCurrent(), sysFileGroupPOPage.getSize());
    }

    @Override
    public SysFileGroupEntity queryById(SysFileGroupId id) {
        SysFileGroupPO sysFileGroupPO = sysFileGroupMapper.selectById(id.getId());
        return sysFileGroupConverter.convertPO2Entity(sysFileGroupPO);
    }

    @Override
    public Boolean removeBatchByIds(List<SysFileGroupId> ids) {
        List<Long> idList = ids.stream().map(SysFileGroupId::getId).toList();
        return SqlHelper.retBool(sysFileGroupMapper.deleteByIds(idList));
    }
}