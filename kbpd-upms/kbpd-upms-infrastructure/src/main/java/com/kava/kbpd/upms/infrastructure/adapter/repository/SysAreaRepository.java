package com.kava.kbpd.upms.infrastructure.adapter.repository;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.common.core.enums.YesNoEnum;
import com.kava.kbpd.upms.domain.model.entity.SysAreaEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysAreaId;
import com.kava.kbpd.upms.domain.model.valobj.SysAreaListQuery;
import com.kava.kbpd.upms.domain.repository.ISysAreaRepository;
import com.kava.kbpd.upms.infrastructure.converter.SysAreaConverter;
import com.kava.kbpd.upms.infrastructure.dao.SysAreaMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysAreaPO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysAreaRepository implements ISysAreaRepository {
    @Resource
    private SysAreaMapper sysAreaMapper;
    @Resource
    private SysAreaConverter sysAreaConverter;


    @Override
    public SysAreaId create(SysAreaEntity sysAreaEntity) {
        SysAreaPO sysAreaPO = sysAreaConverter.convertEntity2PO(sysAreaEntity);
        sysAreaMapper.insert(sysAreaPO);
        return SysAreaId.of(sysAreaPO.getId());
    }

    @Override
    public Boolean update(SysAreaEntity sysAreaEntity) {
        SysAreaPO sysAreaPO = sysAreaConverter.convertEntity2PO(sysAreaEntity);
        return SqlHelper.retBool(sysAreaMapper.updateById(sysAreaPO));
    }

    @Override
    public PagingInfo<SysAreaEntity> queryPage(SysAreaListQuery query) {
        var wrapper = Wrappers.lambdaQuery(SysAreaPO.class)
                .eq(query.getPid() != null, SysAreaPO::getPid, query.getPid())
                .like(query.getName() != null, SysAreaPO::getName, query.getName())
                .eq(query.getAreaType() != null, SysAreaPO::getAreaType, query.getAreaType())
                .eq(query.getAreaStatus() != null, SysAreaPO::getAreaStatus, query.getAreaStatus());
        Page<SysAreaPO> sysAreaPOPage = sysAreaMapper.selectPage(
                Page.of(query.getQueryParam().getPageNo(), query.getQueryParam().getPageSize()),
                wrapper);
        return PagingInfo.toResponse(sysAreaPOPage.getRecords().stream()
                        .map(sysAreaConverter::convertPO2Entity).toList(),
                sysAreaPOPage.getTotal(), sysAreaPOPage.getCurrent(), sysAreaPOPage.getSize());
    }


    @Override
    public SysAreaEntity queryById(SysAreaId id) {
        SysAreaPO sysAreaPO = sysAreaMapper.selectById(id.getId());
        return sysAreaConverter.convertPO2Entity(sysAreaPO);
    }

    @Override
    public List<SysAreaEntity> selectTreeList(SysAreaListQuery query) {
        var wrapper = Wrappers.lambdaQuery(SysAreaPO.class)
                .eq(SysAreaPO::getAreaStatus, YesNoEnum.YES.getCode())
                .in(CollUtil.isNotEmpty(query.getAreaTypes()), SysAreaPO::getAreaType, query.getAreaTypes())
                .orderByDesc(SysAreaPO::getAreaSort);
        List<SysAreaPO> entityList = sysAreaMapper.selectList(wrapper);
        return entityList.stream().map(sysAreaConverter::convertPO2Entity).toList();
    }

    @Override
    public Boolean removeBatchByIds(List<SysAreaId> ids) {
        List<Long> idList = ids.stream().map(SysAreaId::getId).toList();
        return SqlHelper.retBool(sysAreaMapper.deleteByIds(idList));
    }

    @Override
    public List<SysAreaEntity> selectChildren(Long pid) {
        List<SysAreaPO> list = sysAreaMapper.selectList(Wrappers.lambdaQuery(SysAreaPO.class)
                .eq(SysAreaPO::getPid, pid)
                .eq(SysAreaPO::getAreaStatus, YesNoEnum.YES.getCode())
                .orderByDesc(SysAreaPO::getAreaSort));
        return list.stream().map(sysAreaConverter::convertPO2Entity).toList();
    }
}
