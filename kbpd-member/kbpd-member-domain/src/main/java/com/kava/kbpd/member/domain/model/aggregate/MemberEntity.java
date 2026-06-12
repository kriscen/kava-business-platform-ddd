package com.kava.kbpd.member.domain.model.aggregate;

import com.kava.kbpd.common.core.label.AggregateRoot;
import com.kava.kbpd.common.core.model.valobj.MemberId;
import com.kava.kbpd.common.core.model.valobj.SysAppId;
import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberEntity implements AggregateRoot<MemberId> {
    private MemberId id;
    private String mobile;
    private String password;
    private SysTenantId tenantId;
    private SysAppId appId;
    private Boolean enabled;

    @Override
    public MemberId identifier() {
        return id;
    }
}
