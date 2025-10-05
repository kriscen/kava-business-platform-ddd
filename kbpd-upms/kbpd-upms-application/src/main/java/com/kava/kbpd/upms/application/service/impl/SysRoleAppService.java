package com.kava.kbpd.upms.application.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.application.converter.SysRoleAppConverter;
import com.kava.kbpd.upms.application.model.command.SysRoleCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysRoleUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysRoleAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysRoleAppListDTO;
import com.kava.kbpd.upms.application.service.ISysRoleAppService;
import com.kava.kbpd.upms.domain.model.aggregate.SysRoleEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleId;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleListQuery;
import com.kava.kbpd.upms.domain.repository.ISysRoleReadRepository;
import com.kava.kbpd.upms.domain.repository.ISysRoleWriteRepository;
import com.kava.kbpd.upms.domain.service.ISysRoleService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/29
 * @description: role application service
 */
@Slf4j
@Service
public class SysRoleAppService implements ISysRoleAppService {
    @Resource
    private ISysRoleReadRepository readRepository;

    @Resource
    private ISysRoleWriteRepository writeRepository;

    @Resource
    private ISysRoleService sysRoleService;

    @Resource
    private SysRoleAppConverter appConverter;

    @Override
    public SysRoleId createRole(SysRoleCreateCommand command) {
        return writeRepository.create(appConverter.convertCreateCommand2Entity(command));
    }

    @Override
    public void updateRole(SysRoleUpdateCommand command) {
        writeRepository.update(appConverter.convertUpdateCommand2Entity(command));
    }

    @Override
    public void removeRoleBatchByIds(List<SysRoleId> ids) {
        writeRepository.removeBatchByIds(ids);
    }

    @Override
    public PagingInfo<SysRoleAppListDTO> queryRolePage(SysRoleListQuery query) {
        PagingInfo<SysRoleEntity> pagingInfo = readRepository.queryPage(query);
        List<SysRoleAppListDTO> convertList = pagingInfo.getList().stream().map(appConverter::convertEntity2DTO).toList();
        return PagingInfo.toResponse(convertList, pagingInfo);
    }

    @Override
    public SysRoleAppDetailDTO queryRoleById(SysRoleId id) {
        SysRoleEntity sysRoleEntity = readRepository.queryById(id);
        return appConverter.convertEntity2Detail(sysRoleEntity);
    }
}
