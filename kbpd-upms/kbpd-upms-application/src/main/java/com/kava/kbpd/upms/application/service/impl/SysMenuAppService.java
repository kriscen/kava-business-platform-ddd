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
import com.kava.kbpd.upms.domain.repository.ISysUserReadRepository;
import com.kava.kbpd.upms.domain.service.ISysMenuService;
import com.kava.kbpd.upms.types.enums.SysMenuScope;
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
        return PagingInfo.toResponse(collect, sysMenuEntityPagingInfo);
    }

    @Override
    public SysMenuAppDetailDTO queryMenuById(SysMenuId id) {
        SysMenuEntity MenuEntity = sysMenuService.queryById(id);
        return sysMenuAppConverter.convertEntityToDetailDTO(MenuEntity);
    }

    @Override
    public List<SysMenuAppListDTO> queryMenuTree(Long userId, Set<String> roles) {
        List<SysMenuEntity> allMenus = sysMenuService.queryAll();

        List<SysMenuEntity> visibleMenus = filterByScope(allMenus, userId, roles);

        visibleMenus.sort(Comparator.comparingInt(m -> m.getSortOrder() != null ? m.getSortOrder() : 0));

        return buildTree(visibleMenus);
    }

    private List<SysMenuEntity> filterByScope(List<SysMenuEntity> allMenus, Long userId, Set<String> roles) {
        boolean isAdmin = roles != null && roles.contains("ROLE_ADMIN");

        if (isAdmin) {
            return allMenus.stream()
                    .filter(m -> SysMenuScope.SYSTEM.getCode().equals(m.getScope())
                            || SysMenuScope.SYSTEM_TENANT.getCode().equals(m.getScope()))
                    .toList();
        }

        Set<Long> assignedMenuIds = getAssignedMenuIds(userId);
        return allMenus.stream()
                .filter(m -> {
                    String scope = m.getScope();
                    if (SysMenuScope.TENANT.getCode().equals(scope)) {
                        return true;
                    }
                    if (SysMenuScope.SYSTEM_TENANT.getCode().equals(scope)) {
                        return m.getId() != null && assignedMenuIds.contains(m.getId().getId());
                    }
                    return false;
                })
                .toList();
    }

    private Set<Long> getAssignedMenuIds(Long userId) {
        List<Long> menuIds = sysUserReadRepository.queryMenuIdsByUserId(userId);
        return new HashSet<>(menuIds);
    }

    private List<SysMenuAppListDTO> buildTree(List<SysMenuEntity> menus) {
        List<SysMenuAppListDTO> dtos = menus.stream()
                .map(sysMenuAppConverter::convertEntityToListQueryDTO)
                .toList();

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
