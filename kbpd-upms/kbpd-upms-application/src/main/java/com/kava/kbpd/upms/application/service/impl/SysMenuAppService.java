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
import com.kava.kbpd.upms.domain.repository.ISysTenantAppRepository;
import com.kava.kbpd.upms.domain.repository.ISysUserReadRepository;
import com.kava.kbpd.upms.domain.service.ISysMenuService;
import com.kava.kbpd.upms.types.enums.SysMenuLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysMenuAppService implements ISysMenuAppService {

    private final ISysMenuService sysMenuService;
    private final SysMenuAppConverter sysMenuAppConverter;
    private final ISysUserReadRepository sysUserReadRepository;
    private final ISysTenantAppRepository sysTenantAppRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysMenuId createMenu(SysMenuCreateCommand command) {
        SysMenuEntity sysMenuEntity = sysMenuAppConverter.convertCreateCommand2Entity(command);
        return sysMenuService.create(sysMenuEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(SysMenuUpdateCommand command) {
        SysMenuEntity sysMenuEntity = sysMenuAppConverter.convertUpdateCommand2Entity(command);
        sysMenuService.update(sysMenuEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeMenuBatchByIds(List<SysMenuId> ids) {
        sysMenuService.removeBatchByIds(ids);
    }

    @Override
    public PagingInfo<SysMenuAppListDTO> queryMenuPage(SysMenuListQuery query) {
        PagingInfo<SysMenuEntity> sysMenuEntityPagingInfo = sysMenuService.queryPage(query);
        List<SysMenuAppListDTO> collect = sysMenuEntityPagingInfo.getList().stream().map(sysMenuAppConverter::convertEntityToListQueryDTO).toList();

        List<Long> pids = collect.stream().map(SysMenuAppListDTO::getParentId).filter(Objects::nonNull).distinct().toList();
        if (!pids.isEmpty()) {
            List<SysMenuId> pidIds = pids.stream().map(SysMenuId::of).toList();
            Map<Long, String> parentNameMap = sysMenuService.queryByIds(pidIds).stream()
                    .collect(Collectors.toMap(m -> m.getId().getId(), SysMenuEntity::getName, (a, b) -> a));
            collect.forEach(dto -> dto.setParentName(parentNameMap.get(dto.getParentId())));
        }

        return PagingInfo.toResponse(collect, sysMenuEntityPagingInfo);
    }

    @Override
    public SysMenuAppDetailDTO queryMenuById(SysMenuId id) {
        SysMenuEntity menuEntity = sysMenuService.queryById(id);
        SysMenuAppDetailDTO dto = sysMenuAppConverter.convertEntityToDetailDTO(menuEntity);
        if (dto != null && menuEntity.getPid() != null) {
            SysMenuEntity parent = sysMenuService.queryById(menuEntity.getPid());
            dto.setParentName(parent != null ? parent.getName() : null);
        }
        return dto;
    }

    @Override
    public List<SysMenuAppListDTO> queryMenuTree(Long userId, Long tenantId, Set<String> roles) {
        List<SysMenuEntity> allMenus = sysMenuService.queryAll();

        List<SysMenuEntity> visibleMenus = filterByLevel(allMenus, userId, tenantId, roles);

        visibleMenus.sort(Comparator.comparingInt(m -> m.getSortOrder() != null ? m.getSortOrder() : 0));

        Map<Long, String> nameMap = allMenus.stream()
                .filter(m -> m.getId() != null)
                .collect(Collectors.toMap(m -> m.getId().getId(), SysMenuEntity::getName, (a, b) -> a));

        return buildTree(visibleMenus, nameMap);
    }

    private List<SysMenuEntity> filterByLevel(List<SysMenuEntity> allMenus, Long userId, Long tenantId, Set<String> roles) {
        boolean isAdmin = roles != null && roles.contains("ROLE_ADMIN");

        if (isAdmin) {
            return allMenus.stream()
                    .filter(m -> SysMenuLevel.PLATFORM.equals(m.getLevel()))
                    .toList();
        }

        List<Long> tenantMenuIds = getTenantMenuIds(tenantId);
        if (tenantMenuIds.isEmpty()) {
            return Collections.emptyList();
        }
        Set<Long> tenantMenuIdSet = new HashSet<>(tenantMenuIds);

        Set<Long> assignedMenuIds = getAssignedMenuIds(userId);
        Set<Long> visibleMenuIds = tenantMenuIdSet.stream()
                .filter(assignedMenuIds::contains)
                .collect(Collectors.toSet());

        return allMenus.stream()
                .filter(m -> SysMenuLevel.TENANT.equals(m.getLevel()) && m.getId() != null
                        && visibleMenuIds.contains(m.getId().getId()))
                .toList();
    }

    private List<Long> getTenantMenuIds(Long tenantId) {
        if (tenantId == null) {
            return Collections.emptyList();
        }
        return sysTenantAppRepository.queryMenuIdsByTenantId(
                com.kava.kbpd.common.core.model.valobj.SysTenantId.of(tenantId));
    }

    private Set<Long> getAssignedMenuIds(Long userId) {
        List<Long> menuIds = sysUserReadRepository.queryMenuIdsByUserId(userId);
        return new HashSet<>(menuIds);
    }

    private List<SysMenuAppListDTO> buildTree(List<SysMenuEntity> menus, Map<Long, String> nameMap) {
        List<SysMenuAppListDTO> dtos = menus.stream()
                .map(sysMenuAppConverter::convertEntityToListQueryDTO)
                .toList();

        for (SysMenuAppListDTO dto : dtos) {
            dto.setParentName(dto.getParentId() != null ? nameMap.get(dto.getParentId()) : null);
        }

        Map<Long, SysMenuAppListDTO> dtoMap = new LinkedHashMap<>();
        for (SysMenuAppListDTO dto : dtos) {
            dtoMap.put(dto.getId(), dto);
        }

        List<SysMenuAppListDTO> roots = new ArrayList<>();
        for (SysMenuAppListDTO dto : dtos) {
            Long parentId = dto.getParentId();
            if (parentId == null || parentId == 0L || !dtoMap.containsKey(parentId)) {
                roots.add(dto);
            } else {
                SysMenuAppListDTO parent = dtoMap.get(parentId);
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                parent.getChildren().add(dto);
            }
        }
        return roots;
    }

}
