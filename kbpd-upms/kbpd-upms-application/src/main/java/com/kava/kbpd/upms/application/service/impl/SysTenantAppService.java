package com.kava.kbpd.upms.application.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.application.converter.SysTenantAppConverter;
import com.kava.kbpd.upms.application.model.command.SysTenantCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysTenantUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysTenantAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysTenantAppListDTO;
import com.kava.kbpd.upms.application.service.ISysTenantAppService;
import com.kava.kbpd.upms.domain.model.entity.SysTenantEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysTenantListQuery;
import com.kava.kbpd.upms.domain.repository.ISysTenantRepository;
import com.kava.kbpd.upms.domain.service.ISysTenantService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: Tenant application service
 */
@Slf4j
@Service
public class SysTenantAppService implements ISysTenantAppService {
    @Resource
    private ISysTenantRepository sysTenantRepository;

    @Resource
    private ISysTenantService sysTenantService;

    @Resource
    private SysTenantAppConverter sysTenantAppConverter;

    @Override
    public SysTenantId createTenant(SysTenantCreateCommand command) {
        SysTenantEntity sysTenantEntity = sysTenantAppConverter.convertCreateCommand2Entity(command);
        return sysTenantRepository.create(sysTenantEntity);
    }

    @Override
    public void updateTenant(SysTenantUpdateCommand command) {
        SysTenantEntity sysTenantEntity = sysTenantAppConverter.convertUpdateCommand2Entity(command);
        sysTenantRepository.update(sysTenantEntity);
    }

    @Override
    public void removeTenantBatchByIds(List<SysTenantId> ids) {
        sysTenantRepository.removeBatchByIds(ids);
    }

    @Override
    public PagingInfo<SysTenantAppListDTO> queryTenantPage(SysTenantListQuery query) {
        PagingInfo<SysTenantEntity> sysTenantEntityPagingInfo = sysTenantRepository.queryPage(query);
        List<SysTenantAppListDTO> collect = sysTenantEntityPagingInfo.getList().stream().map(sysTenantEntity -> sysTenantAppConverter.convertEntityToListQueryDTO(sysTenantEntity)).toList();
        return PagingInfo.toResponse(collect, sysTenantEntityPagingInfo);
    }

    @Override
    public SysTenantAppDetailDTO queryTenantById(SysTenantId id) {
        SysTenantEntity TenantEntity = sysTenantRepository.queryById(id);
        return sysTenantAppConverter.convertEntityToDetailDTO(TenantEntity);
    }

}
