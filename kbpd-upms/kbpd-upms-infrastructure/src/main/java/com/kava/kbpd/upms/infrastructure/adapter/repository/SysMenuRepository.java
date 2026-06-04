package com.kava.kbpd.upms.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysMenuEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysMenuId;
import com.kava.kbpd.upms.domain.model.valobj.SysMenuListQuery;
import com.kava.kbpd.upms.domain.repository.ISysMenuRepository;
import com.kava.kbpd.upms.infrastructure.converter.SysMenuConverter;
import com.kava.kbpd.upms.infrastructure.dao.SysMenuMapper;
import com.kava.kbpd.upms.infrastructure.dao.SysRoleMenuMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysMenuPO;
import com.kava.kbpd.upms.infrastructure.dao.po.SysRoleMenuPO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysMenuRepository implements ISysMenuRepository {

    @Resource
    private SysMenuMapper sysMenuMapper;
    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Resource
    private SysMenuConverter sysMenuConverter;

    @Override
    public SysMenuId create(SysMenuEntity entity) {
        SysMenuPO sysMenuPO = sysMenuConverter.convertEntity2PO(entity);
        sysMenuMapper.insert(sysMenuPO);
        return SysMenuId.builder()
                .id(sysMenuPO.getId())
                .build();
    }

    @Override
    public Boolean update(SysMenuEntity entity) {
        SysMenuPO sysMenuPO = sysMenuConverter.convertEntity2PO(entity);
        return SqlHelper.retBool(sysMenuMapper.updateById(sysMenuPO));
    }

    @Override
    public PagingInfo<SysMenuEntity> queryPage(SysMenuListQuery query) {
        Page<SysMenuPO> sysMenuPOPage = sysMenuMapper.selectPage(
                Page.of(query.getQueryParam().getPageNo(), query.getQueryParam().getPageSize()),
                Wrappers.lambdaQuery(SysMenuPO.class)
                        .like(query.getMenuName() != null, SysMenuPO::getName, query.getMenuName())
                        .eq(query.getType() != null, SysMenuPO::getMenuType, query.getType())
                        .eq(query.getLevel() != null, SysMenuPO::getLevel, query.getLevel()));
        return PagingInfo.toResponse(sysMenuPOPage.getRecords().stream()
                        .map(sysMenuConverter::convertPO2Entity).toList(),
                sysMenuPOPage.getTotal(), sysMenuPOPage.getCurrent(), sysMenuPOPage.getSize());
    }

    @Override
    public SysMenuEntity queryById(SysMenuId id) {
        SysMenuPO sysMenuPO = sysMenuMapper.selectById(id.getId());
        return sysMenuConverter.convertPO2Entity(sysMenuPO);
    }

    @Override
    public Boolean removeBatchByIds(List<SysMenuId> ids) {
        List<Long> idList = ids.stream().map(SysMenuId::getId).toList();
        return SqlHelper.retBool(sysMenuMapper.deleteByIds(idList));
    }

    @Override
    public List<SysMenuEntity> queryAll() {
        return sysMenuMapper.selectList(Wrappers.lambdaQuery(SysMenuPO.class))
                .stream()
                .map(sysMenuConverter::convertPO2Entity)
                .toList();
    }

    @Override
    public List<SysMenuEntity> queryByIds(List<SysMenuId> ids) {
        List<Long> idList = ids.stream().map(SysMenuId::getId).toList();
        if (idList.isEmpty()) {
            return List.of();
        }
        return sysMenuMapper.selectBatchIds(idList)
                .stream()
                .map(sysMenuConverter::convertPO2Entity)
                .toList();
    }

    @Override
    public List<SysMenuEntity> queryByPid(SysMenuId pid) {
        return sysMenuMapper.selectList(
                        Wrappers.lambdaQuery(SysMenuPO.class)
                                .eq(SysMenuPO::getParentId, pid.getId()))
                .stream()
                .map(sysMenuConverter::convertPO2Entity)
                .toList();
    }

    @Override
    public boolean existsRoleReference(SysMenuId menuId) {
        return sysRoleMenuMapper.selectCount(
                        Wrappers.lambdaQuery(SysRoleMenuPO.class)
                                .eq(SysRoleMenuPO::getMenuId, menuId.getId()))
                > 0;
    }
}