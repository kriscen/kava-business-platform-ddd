package com.kava.kbpd.upms.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysGroupEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysGroupId;
import com.kava.kbpd.upms.domain.model.valobj.SysGroupListQuery;
import com.kava.kbpd.upms.domain.repository.ISysGroupRepository;
import com.kava.kbpd.upms.infrastructure.converter.SysGroupConverter;
import com.kava.kbpd.upms.infrastructure.dao.SysGroupMapper;
import com.kava.kbpd.upms.infrastructure.dao.SysUserMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysGroupPO;
import com.kava.kbpd.upms.infrastructure.dao.po.SysUserPO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysGroupRepository implements ISysGroupRepository {

    @Resource
    private SysGroupMapper sysGroupMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysGroupConverter sysGroupConverter;

    @Override
    public SysGroupId create(SysGroupEntity entity) {
        SysGroupPO sysGroupPO = sysGroupConverter.convertEntity2PO(entity);
        sysGroupMapper.insert(sysGroupPO);
        return SysGroupId.builder()
                .id(sysGroupPO.getId())
                .build();
    }

    @Override
    public Boolean update(SysGroupEntity entity) {
        SysGroupPO sysGroupPO = sysGroupConverter.convertEntity2PO(entity);
        return SqlHelper.retBool(sysGroupMapper.updateById(sysGroupPO));
    }

    @Override
    public PagingInfo<SysGroupEntity> queryPage(SysGroupListQuery query) {
        Page<SysGroupPO> sysGroupPOPage = sysGroupMapper.selectPage(
                Page.of(query.getQueryParam().getPageNo(), query.getQueryParam().getPageSize()),
                Wrappers.lambdaQuery(SysGroupPO.class)
                        .like(query.getGroupName() != null, SysGroupPO::getName, query.getGroupName()));
        return PagingInfo.toResponse(sysGroupPOPage.getRecords().stream()
                        .map(sysGroupConverter::convertPO2Entity).toList(),
                sysGroupPOPage.getTotal(), sysGroupPOPage.getCurrent(), sysGroupPOPage.getSize());
    }

    @Override
    public SysGroupEntity queryById(SysGroupId id) {
        SysGroupPO sysGroupPO = sysGroupMapper.selectById(id.getId());
        return sysGroupConverter.convertPO2Entity(sysGroupPO);
    }

    @Override
    public Boolean removeBatchByIds(List<SysGroupId> ids) {
        List<Long> idList = ids.stream().map(SysGroupId::getId).toList();
        return SqlHelper.retBool(sysGroupMapper.deleteByIds(idList));
    }

    @Override
    public List<SysGroupEntity> queryAll() {
        return sysGroupMapper.selectList(Wrappers.lambdaQuery(SysGroupPO.class))
                .stream()
                .map(sysGroupConverter::convertPO2Entity)
                .toList();
    }

    @Override
    public List<SysGroupEntity> queryByIds(List<SysGroupId> ids) {
        List<Long> idList = ids.stream().map(SysGroupId::getId).toList();
        if (idList.isEmpty()) {
            return List.of();
        }
        return sysGroupMapper.selectBatchIds(idList)
                .stream()
                .map(sysGroupConverter::convertPO2Entity)
                .toList();
    }

    @Override
    public List<SysGroupEntity> queryByPid(SysGroupId pid) {
        return sysGroupMapper.selectList(
                        Wrappers.lambdaQuery(SysGroupPO.class)
                                .eq(SysGroupPO::getPid, pid.getId()))
                .stream()
                .map(sysGroupConverter::convertPO2Entity)
                .toList();
    }

    @Override
    public boolean existsUserReference(SysGroupId groupId) {
        return sysUserMapper.selectCount(
                        Wrappers.lambdaQuery(SysUserPO.class)
                                .eq(SysUserPO::getGroupId, groupId.getId()))
                > 0;
    }
}
