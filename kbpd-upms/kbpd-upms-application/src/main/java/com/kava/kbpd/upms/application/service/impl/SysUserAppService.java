package com.kava.kbpd.upms.application.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.common.core.model.valobj.SysUserId;
import com.kava.kbpd.upms.application.converter.SysUserAppConverter;
import com.kava.kbpd.upms.application.model.command.SysUserCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysUserUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysUserAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysUserAppListDTO;
import com.kava.kbpd.upms.application.service.ISysUserAppService;
import com.kava.kbpd.upms.domain.model.aggregate.SysUserEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysUserListQuery;
import com.kava.kbpd.upms.domain.repository.ISysUserReadRepository;
import com.kava.kbpd.upms.domain.repository.ISysUserWriteRepository;
import com.kava.kbpd.upms.domain.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/29
 * @description: user application service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserAppService implements ISysUserAppService {
    private final ISysUserReadRepository readRepository;
    private final ISysUserWriteRepository writeRepository;
    private final ISysUserService sysUserService;
    private final SysUserAppConverter sysUserAppConverter;
    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysUserId createUser(SysUserCreateCommand command) {
        command.setPassword(passwordEncoder.encode(command.getPassword()));
        return sysUserService.create(sysUserAppConverter.convertCreateCommand2Entity(command));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(SysUserUpdateCommand command) {
        if (command.getPassword() != null && !command.getPassword().isEmpty()) {
            command.setPassword(passwordEncoder.encode(command.getPassword()));
        }
        sysUserService.update(sysUserAppConverter.convertUpdateCommand2Entity(command));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeUserBatchByIds(List<SysUserId> ids) {
        sysUserService.removeBatchByIds(ids);
    }

    @Override
    public PagingInfo<SysUserAppListDTO> queryUserPage(SysUserListQuery query) {
        PagingInfo<SysUserEntity> pagingInfo = readRepository.queryPage(query);
        List<SysUserAppListDTO> convertList = pagingInfo.getList().stream().map(sysUserAppConverter::convertEntity2DTO).toList();
        return PagingInfo.toResponse(convertList, pagingInfo);
    }

    @Override
    public SysUserAppDetailDTO queryUserById(SysUserId id) {
        SysUserEntity sysUserEntity = readRepository.queryById(id);
        return sysUserAppConverter.convertEntity2Detail(sysUserEntity);
    }

    @Override
    public SysUserAppDetailDTO queryUserByUsername(Long tenantId, String username) {
        SysUserEntity sysUserEntity = readRepository.queryByUsername(tenantId, username);
        if (sysUserEntity == null) {
            return null;
        }
        return sysUserAppConverter.convertEntity2Detail(sysUserEntity);
    }

    @Override
    public List<String> queryRoleCodesByUserId(Long userId) {
        return readRepository.queryRoleCodesByUserId(userId);
    }

    @Override
    public List<String> queryPermissionsByUserId(Long userId) {
        return readRepository.queryPermissionsByUserId(userId);
    }

    @Override
    public String queryDataScopeByUserId(Long userId) {
        return readRepository.queryDataScopeByUserId(userId);
    }

    @Override
    public List<Long> queryMenuIdsByUserId(Long userId) {
        return readRepository.queryMenuIdsByUserId(userId);
    }
}
