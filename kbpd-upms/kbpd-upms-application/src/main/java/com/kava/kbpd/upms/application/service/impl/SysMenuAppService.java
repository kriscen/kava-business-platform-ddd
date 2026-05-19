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
import com.kava.kbpd.upms.domain.repository.ISysUserReadRepository;
import com.kava.kbpd.upms.types.enums.SysMenuScope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: Menu application service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysMenuAppService implements ISysMenuAppService {
    private final ISysMenuRepository sysMenuRepository;
    private final SysMenuAppConverter sysMenuAppConverter;
    private final ISysUserReadRepository sysUserReadRepository;

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

    @Override
    public List<SysMenuAppListDTO> queryMenuTree(Long userId, Set<String> roles) {
        List<SysMenuEntity> allMenus = sysMenuRepository.queryAll();

        // 按 scope 过滤可见菜单
        List<SysMenuEntity> visibleMenus = filterByScope(allMenus, userId, roles);

        // 按 sortOrder 排序
        visibleMenus.sort(Comparator.comparingInt(m -> m.getSortOrder() != null ? m.getSortOrder() : 0));

        // 构建树结构
        return buildTree(visibleMenus);
    }

    /**
     * 根据用户角色和菜单 scope 过滤
     */
    private List<SysMenuEntity> filterByScope(List<SysMenuEntity> allMenus, Long userId, Set<String> roles) {
        boolean isAdmin = roles != null && roles.contains("ROLE_ADMIN");

        if (isAdmin) {
            // 平台管理员看 SYSTEM + SYSTEM_TENANT
            return allMenus.stream()
                    .filter(m -> SysMenuScope.SYSTEM.getCode().equals(m.getScope())
                            || SysMenuScope.SYSTEM_TENANT.getCode().equals(m.getScope()))
                    .toList();
        }

        // 租户用户可见: TENANT + 已分配的 SYSTEM_TENANT
        Set<Long> assignedMenuIds = getAssignedMenuIds(userId);
        return allMenus.stream()
                .filter(m -> {
                    String scope = m.getScope();
                    // TENANT 菜单始终可见
                    if (SysMenuScope.TENANT.getCode().equals(scope)) {
                        return true;
                    }
                    // SYSTEM_TENANT 菜单只有被分配了才可见
                    if (SysMenuScope.SYSTEM_TENANT.getCode().equals(scope)) {
                        return m.getId() != null && assignedMenuIds.contains(m.getId().getId());
                    }
                    // SYSTEM 菜单租户用户不可见
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

        // 用 id -> dto 映射
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
