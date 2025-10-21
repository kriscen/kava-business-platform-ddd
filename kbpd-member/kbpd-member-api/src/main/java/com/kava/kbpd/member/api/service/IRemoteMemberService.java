package com.kava.kbpd.member.api.service;

import com.kava.kbpd.member.api.model.dto.MemberInfoDTO;

/**
 * @author Kris
 * @date 2025/4/2
 * @description: remote member service
 */
public interface IRemoteMemberService {
    MemberInfoDTO findMemberByMobile(String tenantId,String mobile);

}
