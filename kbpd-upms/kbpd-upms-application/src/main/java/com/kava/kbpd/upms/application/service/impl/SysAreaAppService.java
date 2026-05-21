package com.kava.kbpd.upms.application.service.impl;

import com.kava.kbpd.common.core.model.tree.Tree;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.application.converter.SysAreaAppConverter;
import com.kava.kbpd.upms.application.model.command.SysAreaCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysAreaUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysAreaAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysAreaAppListDTO;
import com.kava.kbpd.upms.application.service.ISysAreaAppService;
import com.kava.kbpd.upms.domain.model.entity.SysAreaEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysAreaId;
import com.kava.kbpd.upms.domain.model.valobj.SysAreaListQuery;
import com.kava.kbpd.upms.domain.repository.ISysAreaRepository;
import com.kava.kbpd.upms.domain.service.ISysAreaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: area application service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysAreaAppService implements ISysAreaAppService {
    private final ISysAreaRepository sysAreaRepository;
    private final ISysAreaService sysAreaService;
    private final SysAreaAppConverter sysAreaAppConverter;

    @Override
    public SysAreaId createArea(SysAreaCreateCommand command) {
        SysAreaEntity sysAreaEntity = sysAreaAppConverter.convertCreateCommand2Entity(command);
        return sysAreaRepository.create(sysAreaEntity);
    }

    @Override
    public void updateArea(SysAreaUpdateCommand command) {
        SysAreaEntity sysAreaEntity = sysAreaAppConverter.convertUpdateCommand2Entity(command);
        sysAreaRepository.update(sysAreaEntity);
    }

    @Override
    public void removeAreaBatchByIds(List<SysAreaId> ids) {
        sysAreaRepository.removeBatchByIds(ids);
    }

    @Override
    public PagingInfo<SysAreaAppListDTO> queryAreaPage(SysAreaListQuery query) {
        PagingInfo<SysAreaEntity> sysAreaEntityPagingInfo = sysAreaRepository.queryPage(query);
        List<SysAreaAppListDTO> collect = sysAreaEntityPagingInfo.getList().stream().map(sysAreaEntity -> sysAreaAppConverter.convertEntityToListQueryDTO(sysAreaEntity)).toList();
        return PagingInfo.toResponse(collect, sysAreaEntityPagingInfo);
    }

    @Override
    public SysAreaAppDetailDTO queryAreaById(SysAreaId id) {
        SysAreaEntity areaEntity = sysAreaRepository.queryById(id);
        return sysAreaAppConverter.convertEntityToDetailDTO(areaEntity);
    }

    @Override
    public List<Tree<Long>> selectAreaTree(SysAreaListQuery query) {
        return sysAreaService.selectAreaTree(query);
    }

    @Override
    public List<SysAreaAppListDTO> queryAreaChildren(SysAreaId pid) {
        List<SysAreaEntity> children = sysAreaService.selectChildren(pid);
        return children.stream().map(sysAreaAppConverter::convertEntityToListQueryDTO).toList();
    }

}
