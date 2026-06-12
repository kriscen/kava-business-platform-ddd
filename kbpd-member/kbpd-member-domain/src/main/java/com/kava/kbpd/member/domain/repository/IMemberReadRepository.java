package com.kava.kbpd.member.domain.repository;

import com.kava.kbpd.common.core.model.valobj.MemberId;
import com.kava.kbpd.common.core.model.valobj.SysAppId;
import com.kava.kbpd.member.domain.model.aggregate.MemberEntity;

public interface IMemberReadRepository {
    MemberEntity queryById(MemberId id);
    MemberEntity queryByMobileAndAppId(String mobile, SysAppId appId);
}
