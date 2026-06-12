package com.kava.kbpd.member.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kava.kbpd.common.core.model.valobj.MemberId;
import com.kava.kbpd.common.core.model.valobj.SysAppId;
import com.kava.kbpd.member.domain.model.aggregate.MemberEntity;
import com.kava.kbpd.member.domain.repository.IMemberReadRepository;
import com.kava.kbpd.member.infrastructure.converter.MemberConverter;
import com.kava.kbpd.member.infrastructure.dao.MemberMapper;
import com.kava.kbpd.member.infrastructure.dao.po.MemberPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberReadRepository implements IMemberReadRepository {

    private final MemberMapper memberMapper;
    private final MemberConverter memberConverter;

    @Override
    public MemberEntity queryById(MemberId id) {
        MemberPO po = memberMapper.selectById(id.getId());
        return po == null ? null : memberConverter.convertPO2Entity(po);
    }

    @Override
    public MemberEntity queryByMobileAndAppId(String mobile, SysAppId appId) {
        LambdaQueryWrapper<MemberPO> wrapper = new LambdaQueryWrapper<MemberPO>()
                .eq(MemberPO::getMobile, mobile)
                .eq(MemberPO::getAppId, appId.getId());
        MemberPO po = memberMapper.selectOne(wrapper);
        return po == null ? null : memberConverter.convertPO2Entity(po);
    }
}
