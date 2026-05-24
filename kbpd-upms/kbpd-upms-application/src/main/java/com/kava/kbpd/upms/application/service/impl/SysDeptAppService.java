package com.kava.kbpd.upms.application.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.application.converter.SysDeptAppConverter;
import com.kava.kbpd.upms.application.model.command.SysDeptCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysDeptUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysDeptAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysDeptAppListDTO;
import com.kava.kbpd.upms.application.service.ISysDeptAppService;
import com.kava.kbpd.upms.domain.model.entity.SysDeptEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysDeptId;
import com.kava.kbpd.upms.domain.model.valobj.SysDeptListQuery;
import com.kava.kbpd.upms.domain.service.ISysDeptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: dept application service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysDeptAppService implements ISysDeptAppService {
    private final ISysDeptService sysDeptService;
    private final SysDeptAppConverter sysDeptAppConverter;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysDeptId createDept(SysDeptCreateCommand command) {
        SysDeptEntity sysDeptEntity = sysDeptAppConverter.convertCreateCommand2Entity(command);
        return sysDeptService.create(sysDeptEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDept(SysDeptUpdateCommand command) {
        SysDeptEntity sysDeptEntity = sysDeptAppConverter.convertUpdateCommand2Entity(command);
        sysDeptService.update(sysDeptEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeDeptBatchByIds(List<SysDeptId> ids) {
        sysDeptService.removeBatchByIds(ids);
    }

    @Override
    public PagingInfo<SysDeptAppListDTO> queryDeptPage(SysDeptListQuery query) {
        PagingInfo<SysDeptEntity> sysDeptEntityPagingInfo = sysDeptService.queryPage(query);
        List<SysDeptAppListDTO> collect = sysDeptEntityPagingInfo.getList().stream().map(sysDeptAppConverter::convertEntityToListQueryDTO).toList();
        return PagingInfo.toResponse(collect, sysDeptEntityPagingInfo);
    }

    @Override
    public SysDeptAppDetailDTO queryDeptById(SysDeptId id) {
        SysDeptEntity deptEntity = sysDeptService.queryById(id);
        return sysDeptAppConverter.convertEntityToDetailDTO(deptEntity);
    }

}
