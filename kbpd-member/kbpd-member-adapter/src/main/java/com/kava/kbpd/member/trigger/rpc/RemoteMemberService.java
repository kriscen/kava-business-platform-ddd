package com.kava.kbpd.member.trigger.rpc;

import com.kava.kbpd.member.api.model.dto.MemberInfoDTO;
import com.kava.kbpd.member.api.service.IRemoteMemberService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author Kris
 * @date 2025/4/7
 * @description:
 */
@DubboService(version = "1.0")
public class RemoteMemberService implements IRemoteMemberService {
    @Override
    public MemberInfoDTO findMemberByMobile(String tenantId,String mobile) {
        //TODO Member 模块 DDD 结构尚未搭建（无 domain/infrastructure/application 层），待搭建后接通真实查询
        return null;
    }

}
