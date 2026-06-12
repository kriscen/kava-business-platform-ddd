package com.kava.kbpd.member.infrastructure.converter;

import com.kava.kbpd.member.domain.model.aggregate.MemberEntity;
import com.kava.kbpd.member.infrastructure.dao.po.MemberPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberConverter {

    @Mapping(source = "id", target = "id.id")
    @Mapping(source = "tenantId", target = "tenantId.id")
    @Mapping(source = "appId", target = "appId.id")
    MemberEntity convertPO2Entity(MemberPO memberPO);

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "tenantId.id", target = "tenantId")
    @Mapping(source = "appId.id", target = "appId")
    MemberPO convertEntity2PO(MemberEntity entity);
}
