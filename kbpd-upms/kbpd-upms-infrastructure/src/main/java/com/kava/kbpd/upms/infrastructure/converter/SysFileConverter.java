package com.kava.kbpd.upms.infrastructure.converter;

import com.kava.kbpd.upms.domain.basic.model.entity.SysFileEntity;
import com.kava.kbpd.upms.infrastructure.dao.po.SysFilePO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: SysFile转换器
 */
@Mapper(componentModel = "spring")
public interface SysFileConverter {

    @Mapping(source = "id", target = "id.id")
    SysFileEntity convertPO2Entity(SysFilePO sysFilePO);

    @Mapping(source = "id.id", target = "id")
    SysFilePO convertEntity2PO(SysFileEntity entity);
}