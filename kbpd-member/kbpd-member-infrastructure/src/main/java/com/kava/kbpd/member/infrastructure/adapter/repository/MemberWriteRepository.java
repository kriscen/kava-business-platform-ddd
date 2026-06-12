package com.kava.kbpd.member.infrastructure.adapter.repository;

import com.kava.kbpd.common.core.model.valobj.MemberId;
import com.kava.kbpd.member.domain.model.aggregate.MemberEntity;
import com.kava.kbpd.member.domain.repository.IMemberWriteRepository;
import com.kava.kbpd.member.infrastructure.converter.MemberConverter;
import com.kava.kbpd.member.infrastructure.dao.MemberMapper;
import com.kava.kbpd.member.infrastructure.dao.po.MemberPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberWriteRepository implements IMemberWriteRepository {

    private final MemberMapper memberMapper;
    private final MemberConverter memberConverter;

    @Override
    public MemberId create(MemberEntity entity) {
        MemberPO po = memberConverter.convertEntity2PO(entity);
        memberMapper.insert(po);
        return MemberId.of(po.getId());
    }

    @Override
    public Boolean update(MemberEntity entity) {
        MemberPO po = memberConverter.convertEntity2PO(entity);
        return memberMapper.updateById(po) > 0;
    }

    @Override
    public Boolean delete(MemberId id) {
        return memberMapper.deleteById(id.getId()) > 0;
    }
}
