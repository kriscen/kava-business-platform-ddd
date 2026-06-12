package com.kava.kbpd.upms.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kava.kbpd.common.core.model.valobj.SysAppId;
import com.kava.kbpd.upms.domain.repository.ISysAppMenuRepository;
import com.kava.kbpd.upms.infrastructure.dao.SysAppMenuMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysAppMenuPO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class SysAppMenuRepository implements ISysAppMenuRepository {

    @Resource
    private SysAppMenuMapper sysAppMenuMapper;

    @Override
    public void replaceAppMenus(SysAppId appId, List<Long> menuIds) {
        sysAppMenuMapper.delete(
                Wrappers.lambdaQuery(SysAppMenuPO.class)
                        .eq(SysAppMenuPO::getAppId, appId.getId()));

        if (menuIds != null && !menuIds.isEmpty()) {
            List<SysAppMenuPO> pos = menuIds.stream()
                    .map(menuId -> {
                        SysAppMenuPO po = new SysAppMenuPO();
                        po.setAppId(appId.getId());
                        po.setMenuId(menuId);
                        return po;
                    })
                    .toList();
            for (SysAppMenuPO po : pos) {
                sysAppMenuMapper.insert(po);
            }
        }
    }

    @Override
    public List<Long> queryMenuIdsByAppId(SysAppId appId) {
        return sysAppMenuMapper.selectList(
                        Wrappers.lambdaQuery(SysAppMenuPO.class)
                                .eq(SysAppMenuPO::getAppId, appId.getId()))
                .stream()
                .map(SysAppMenuPO::getMenuId)
                .toList();
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
