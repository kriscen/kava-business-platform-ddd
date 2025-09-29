package com.kava.kbpd.upms.application.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.common.core.model.valobj.SysUserId;
import com.kava.kbpd.upms.application.converter.SysUserAppConverter;
import com.kava.kbpd.upms.application.model.command.SysUserCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysUserUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysUserAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysUserListQueryDTO;
import com.kava.kbpd.upms.application.service.ISysUserAppService;
import com.kava.kbpd.upms.domain.model.aggregate.SysUserEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysUserListQuery;
import com.kava.kbpd.upms.domain.repository.ISysUserReadRepository;
import com.kava.kbpd.upms.domain.repository.ISysUserWriteRepository;
import com.kava.kbpd.upms.domain.service.ISysUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/29
 * @description: user application service
 */
@Slf4j
@Service
public class SysUserAppService implements ISysUserAppService {
    @Resource
    private ISysUserReadRepository readRepository;

    @Resource
    private ISysUserWriteRepository writeRepository;

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private SysUserAppConverter sysUserAppConverter;

    @Override
    public SysUserId createUser(SysUserCreateCommand command) {
        return writeRepository.create(sysUserAppConverter.convertCreateCommand2Entity(command));
    }

    @Override
    public void updateUser(SysUserUpdateCommand command) {
        writeRepository.update(sysUserAppConverter.convertUpdateCommand2Entity(command));
    }

    @Override
    public void removeUserBatchByIds(List<SysUserId> ids) {
        writeRepository.removeBatchByIds(ids);
    }

    @Override
    public PagingInfo<SysUserListQueryDTO> queryUserPage(SysUserListQuery query) {
        PagingInfo<SysUserEntity> pagingInfo = readRepository.queryPage(query);
        List<SysUserListQueryDTO> convertList = pagingInfo.getList().stream().map(sysUserAppConverter::convertEntity2DTO).toList();
        return PagingInfo.toResponse(convertList, pagingInfo);
    }

    @Override
    public SysUserAppDetailDTO queryUserById(SysUserId id) {
        SysUserEntity sysUserEntity = readRepository.queryById(id);
        return sysUserAppConverter.convertEntity2Detail(sysUserEntity);
    }
}
