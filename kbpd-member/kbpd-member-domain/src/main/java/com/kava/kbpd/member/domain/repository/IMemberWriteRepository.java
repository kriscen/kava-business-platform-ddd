package com.kava.kbpd.member.domain.repository;

import com.kava.kbpd.common.core.model.valobj.MemberId;
import com.kava.kbpd.member.domain.model.aggregate.MemberEntity;

public interface IMemberWriteRepository {
    MemberId create(MemberEntity entity);
    Boolean update(MemberEntity entity);
    Boolean delete(MemberId id);
}
