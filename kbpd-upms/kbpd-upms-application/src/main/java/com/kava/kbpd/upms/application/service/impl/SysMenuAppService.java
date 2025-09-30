package com.kava.kbpd.upms.application.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.application.converter.SysMenuAppConverter;
import com.kava.kbpd.upms.application.model.command.SysMenuCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysMenuUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysMenuAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysMenuAppListDTO;
import com.kava.kbpd.upms.application.service.ISysMenuAppService;
import com.kava.kbpd.upms.domain.model.entity.SysMenuEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysMenuId;
import com.kava.kbpd.upms.domain.model.valobj.SysMenuListQuery;
import com.kava.kbpd.upms.domain.repository.ISysMenuRepository;
import com.kava.kbpd.upms.domain.service.ISysMenuService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: Menu application service
 */
@Slf4j
@Service
public class SysMenuAppService implements ISysMenuAppService {
    @Resource
    private ISysMenuRepository sysMenuRepository;

    @Resource
    private ISysMenuService sysMenuService;

    @Resource
    private SysMenuAppConverter sysMenuAppConverter;

    @Override
    public SysMenuId createMenu(SysMenuCreateCommand command) {
        SysMenuEntity sysMenuEntity = sysMenuAppConverter.convertCreateCommand2Entity(command);
        return sysMenuRepository.create(sysMenuEntity);
    }

    @Override
    public void updateMenu(SysMenuUpdateCommand command) {
        SysMenuEntity sysMenuEntity = sysMenuAppConverter.convertUpdateCommand2Entity(command);
        sysMenuRepository.update(sysMenuEntity);
    }

    @Override
    public void removeMenuBatchByIds(List<SysMenuId> ids) {
        sysMenuRepository.removeBatchByIds(ids);
    }

    @Override
    public PagingInfo<SysMenuAppListDTO> queryMenuPage(SysMenuListQuery query) {
        PagingInfo<SysMenuEntity> sysMenuEntityPagingInfo = sysMenuRepository.queryPage(query);
        List<SysMenuAppListDTO> collect = sysMenuEntityPagingInfo.getList().stream().map(sysMenuEntity -> sysMenuAppConverter.convertEntityToListQueryDTO(sysMenuEntity)).toList();
        return PagingInfo.toResponse(collect, sysMenuEntityPagingInfo);
    }

    @Override
    public SysMenuAppDetailDTO queryMenuById(SysMenuId id) {
        SysMenuEntity MenuEntity = sysMenuRepository.queryById(id);
        return sysMenuAppConverter.convertEntityToDetailDTO(MenuEntity);
    }

}
