package com.kava.kbpd.upms.application.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.common.core.base.QueryParamValObj;
import com.kava.kbpd.upms.application.model.command.SysAppCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysAppUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysAppListDTO;
import com.kava.kbpd.upms.application.service.ISysAppAppService;
import com.kava.kbpd.upms.domain.model.entity.SysAppEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysAppId;
import com.kava.kbpd.upms.domain.model.valobj.SysAppListQuery;
import com.kava.kbpd.upms.domain.repository.ISysAppMenuRepository;
import com.kava.kbpd.upms.domain.service.ISysAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysAppAppService implements ISysAppAppService {
    private final ISysAppService sysAppService;
    private final ISysAppMenuRepository sysAppMenuRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createApp(SysAppCreateCommand command) {
        SysAppEntity entity = SysAppEntity.builder()
                .code(command.getCode())
                .name(command.getName())
                .icon(command.getIcon())
                .description(command.getDescription())
                .build();
        SysAppId appId = sysAppService.create(entity);
        return appId.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateApp(SysAppUpdateCommand command) {
        SysAppEntity entity = SysAppEntity.builder()
                .id(SysAppId.of(command.getId()))
                .code(command.getCode())
                .name(command.getName())
                .icon(command.getIcon())
                .description(command.getDescription())
                .build();
        sysAppService.update(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeAppBatchByIds(List<Long> ids) {
        List<SysAppId> appIds = ids.stream().map(SysAppId::of).toList();
        sysAppService.removeBatchByIds(appIds);
    }

    @Override
    public PagingInfo<SysAppListDTO> queryAppPage(String appName, Integer pageNo, Integer pageSize) {
        SysAppListQuery query = SysAppListQuery.builder()
                .queryParam(QueryParamValObj.builder().pageNo(pageNo).pageSize(pageSize).build())
                .appName(appName)
                .build();
        PagingInfo<SysAppEntity> pagingInfo = sysAppService.queryPage(query);
        List<SysAppListDTO> dtos = pagingInfo.getList().stream()
                .map(this::convertToListDTO)
                .toList();
        return PagingInfo.toResponse(dtos, pagingInfo);
    }

    @Override
    public SysAppDetailDTO queryAppById(Long id) {
        SysAppEntity entity = sysAppService.queryById(SysAppId.of(id));
        return entity != null ? convertToDetailDTO(entity) : null;
    }

    @Override
    public List<SysAppListDTO> queryAppDropdown() {
        return sysAppService.queryAll().stream()
                .map(this::convertToListDTO)
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAppMenus(Long appId, List<Long> menuIds) {
        sysAppMenuRepository.replaceAppMenus(SysAppId.of(appId), menuIds);
    }

    private SysAppListDTO convertToListDTO(SysAppEntity entity) {
        SysAppListDTO dto = new SysAppListDTO();
        dto.setId(entity.getId().getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setIcon(entity.getIcon());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().getCode() : null);
        return dto;
    }

    private SysAppDetailDTO convertToDetailDTO(SysAppEntity entity) {
        SysAppDetailDTO dto = new SysAppDetailDTO();
        dto.setId(entity.getId().getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setIcon(entity.getIcon());
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().getCode() : null);
        return dto;
    }
}
