package com.kava.kbpd.upms.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.domain.model.entity.SysTenantAppEntity;
import com.kava.kbpd.common.core.model.valobj.SysAppId;
import com.kava.kbpd.upms.domain.model.valobj.SysTenantAppId;
import com.kava.kbpd.upms.domain.repository.ISysTenantAppRepository;
import com.kava.kbpd.upms.infrastructure.converter.SysTenantAppConverter;
import com.kava.kbpd.upms.infrastructure.dao.SysAppMenuMapper;
import com.kava.kbpd.upms.infrastructure.dao.SysTenantAppMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysAppMenuPO;
import com.kava.kbpd.upms.infrastructure.dao.po.SysTenantAppPO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class SysTenantAppRepository implements ISysTenantAppRepository {

    @Resource
    private SysTenantAppMapper sysTenantAppMapper;
    @Resource
    private SysAppMenuMapper sysAppMenuMapper;
    @Resource
    private SysTenantAppConverter sysTenantAppConverter;

    @Override
    public SysTenantAppId create(SysTenantAppEntity entity) {
        SysTenantAppPO po = sysTenantAppConverter.convertEntity2PO(entity);
        sysTenantAppMapper.insert(po);
        return SysTenantAppId.builder().id(po.getId()).build();
    }

    @Override
    public Boolean update(SysTenantAppEntity entity) {
        SysTenantAppPO po = sysTenantAppConverter.convertEntity2PO(entity);
        return SqlHelper.retBool(sysTenantAppMapper.updateById(po));
    }

    @Override
    public List<SysTenantAppEntity> queryByTenantId(SysTenantId tenantId) {
        return sysTenantAppMapper.selectList(
                        Wrappers.lambdaQuery(SysTenantAppPO.class)
                                .eq(SysTenantAppPO::getTenantId, tenantId.getId()))
                .stream()
                .map(sysTenantAppConverter::convertPO2Entity)
                .toList();
    }

    @Override
    public SysTenantAppEntity queryByTenantIdAndAppId(SysTenantId tenantId, SysAppId appId) {
        SysTenantAppPO po = sysTenantAppMapper.selectOne(
                Wrappers.lambdaQuery(SysTenantAppPO.class)
                        .eq(SysTenantAppPO::getTenantId, tenantId.getId())
                        .eq(SysTenantAppPO::getAppId, appId.getId()));
        return po != null ? sysTenantAppConverter.convertPO2Entity(po) : null;
    }

    @Override
    public boolean existsActiveSubscription(SysTenantId tenantId, SysAppId appId) {
        return sysTenantAppMapper.selectCount(
                Wrappers.lambdaQuery(SysTenantAppPO.class)
                        .eq(SysTenantAppPO::getTenantId, tenantId.getId())
                        .eq(SysTenantAppPO::getAppId, appId.getId())
                        .eq(SysTenantAppPO::getStatus, "ACTIVE")) > 0;
    }

    @Override
    public List<Long> queryMenuIdsByTenantId(SysTenantId tenantId) {
        List<Long> appIds = sysTenantAppMapper.selectList(
                        Wrappers.lambdaQuery(SysTenantAppPO.class)
                                .eq(SysTenantAppPO::getTenantId, tenantId.getId())
                                .eq(SysTenantAppPO::getStatus, "ACTIVE"))
                .stream()
                .map(SysTenantAppPO::getAppId)
                .toList();

        if (appIds.isEmpty()) {
            return Collections.emptyList();
        }

        return queryMenuIdsByAppIds(appIds);
    }

    @Override
    public List<Long> queryMenuIdsByAppIds(List<Long> appIds) {
        if (appIds == null || appIds.isEmpty()) {
            return Collections.emptyList();
        }

        return sysAppMenuMapper.selectList(
                        Wrappers.lambdaQuery(SysAppMenuPO.class)
                                .in(SysAppMenuPO::getAppId, appIds))
                .stream()
                .map(SysAppMenuPO::getMenuId)
                .distinct()
                .toList();
    }
}
