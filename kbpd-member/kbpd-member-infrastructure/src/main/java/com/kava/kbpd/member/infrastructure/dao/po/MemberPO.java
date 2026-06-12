package com.kava.kbpd.member.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kava.kbpd.common.database.po.TenantDeletablePO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("mbr_member")
public class MemberPO extends TenantDeletablePO {
    private String mobile;
    private String password;
    private Long appId;
    private Boolean enabled;
}
