package com.kava.kbpd.upms.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysAppEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysAppId;
import com.kava.kbpd.upms.domain.model.valobj.SysAppListQuery;
import com.kava.kbpd.upms.domain.repository.ISysAppRepository;
import com.kava.kbpd.upms.infrastructure.converter.SysAppConverter;
import com.kava.kbpd.upms.infrastructure.dao.SysAppMapper;
import com.kava.kbpd.upms.infrastructure.dao.SysTenantAppMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysAppPO;
import com.kava.kbpd.upms.infrastructure.dao.po.SysTenantAppPO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysAppRepository implements ISysAppRepository {

    @Resource
    private SysAppMapper sysAppMapper;
    @Resource
    private SysAppConverter sysAppConverter;
    @Resource
    private SysTenantAppMapper sysTenantAppMapper;

    @Override
    public SysAppId create(SysAppEntity entity) {
        SysAppPO po = sysAppConverter.convertEntity2PO(entity);
        sysAppMapper.insert(po);
        return SysAppId.builder().id(po.getId()).build();
    }

    @Override
    public Boolean update(SysAppEntity entity) {
        SysAppPO po = sysAppConverter.convertEntity2PO(entity);
        return SqlHelper.retBool(sysAppMapper.updateById(po));
    }

    @Override
    public Boolean removeBatchByIds(List<SysAppId> ids) {
        List<Long> idList = ids.stream().map(SysAppId::getId).toList();
        return SqlHelper.retBool(sysAppMapper.deleteByIds(idList));
    }

    @Override
    public PagingInfo<SysAppEntity> queryPage(SysAppListQuery query) {
        Page<SysAppPO> page = sysAppMapper.selectPage(
                Page.of(query.getQueryParam().getPageNo(), query.getQueryParam().getPageSize()),
                Wrappers.lambdaQuery(SysAppPO.class)
                        .like(query.getAppName() != null, SysAppPO::getName, query.getAppName()));
        return PagingInfo.toResponse(page.getRecords().stream()
                        .map(sysAppConverter::convertPO2Entity).toList(),
                page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public SysAppEntity queryById(SysAppId id) {
        SysAppPO po = sysAppMapper.selectById(id.getId());
        return po != null ? sysAppConverter.convertPO2Entity(po) : null;
    }

    @Override
    public SysAppEntity queryByCode(String code) {
        SysAppPO po = sysAppMapper.selectOne(
                Wrappers.lambdaQuery(SysAppPO.class)
                        .eq(SysAppPO::getCode, code));
        return po != null ? sysAppConverter.convertPO2Entity(po) : null;
    }

    @Override
    public List<SysAppEntity> queryAll() {
        return sysAppMapper.selectList(Wrappers.lambdaQuery(SysAppPO.class))
                .stream()
                .map(sysAppConverter::convertPO2Entity)
                .toList();
    }

    @Override
    public boolean existsActiveTenantApp(SysAppId appId) {
        return sysTenantAppMapper.selectCount(
                Wrappers.lambdaQuery(SysTenantAppPO.class)
                        .eq(SysTenantAppPO::getAppId, appId.getId())
                        .eq(SysTenantAppPO::getStatus, "ACTIVE")) > 0;
    }
}
