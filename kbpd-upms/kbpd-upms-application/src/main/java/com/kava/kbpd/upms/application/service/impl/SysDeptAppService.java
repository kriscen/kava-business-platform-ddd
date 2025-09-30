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
import com.kava.kbpd.upms.domain.repository.ISysDeptRepository;
import com.kava.kbpd.upms.domain.service.ISysDeptService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: dept application service
 */
@Slf4j
@Service
public class SysDeptAppService implements ISysDeptAppService {
    @Resource
    private ISysDeptRepository sysDeptRepository;

    @Resource
    private ISysDeptService sysDeptService;

    @Resource
    private SysDeptAppConverter sysDeptAppConverter;

    @Override
    public SysDeptId createDept(SysDeptCreateCommand command) {
        SysDeptEntity sysDeptEntity = sysDeptAppConverter.convertCreateCommand2Entity(command);
        return sysDeptRepository.create(sysDeptEntity);
    }

    @Override
    public void updateDept(SysDeptUpdateCommand command) {
        SysDeptEntity sysDeptEntity = sysDeptAppConverter.convertUpdateCommand2Entity(command);
        sysDeptRepository.update(sysDeptEntity);
    }

    @Override
    public void removeDeptBatchByIds(List<SysDeptId> ids) {
        sysDeptRepository.removeBatchByIds(ids);
    }

    @Override
    public PagingInfo<SysDeptAppListDTO> queryDeptPage(SysDeptListQuery query) {
        PagingInfo<SysDeptEntity> sysDeptEntityPagingInfo = sysDeptRepository.queryPage(query);
        List<SysDeptAppListDTO> collect = sysDeptEntityPagingInfo.getList().stream().map(sysDeptEntity -> sysDeptAppConverter.convertEntityToListQueryDTO(sysDeptEntity)).toList();
        return PagingInfo.toResponse(collect, sysDeptEntityPagingInfo);
    }

    @Override
    public SysDeptAppDetailDTO queryDeptById(SysDeptId id) {
        SysDeptEntity DeptEntity = sysDeptRepository.queryById(id);
        return sysDeptAppConverter.convertEntityToDetailDTO(DeptEntity);
    }

}
