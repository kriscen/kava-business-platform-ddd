package com.kava.kbpd.upms.domain.model.entity;

import com.kava.kbpd.common.core.label.Entity;
import com.kava.kbpd.upms.domain.model.valobj.SysAppId;
import com.kava.kbpd.upms.types.enums.SysAppStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysAppEntity implements Entity<SysAppId> {

    private SysAppId id;
    private String code;
    private String name;
    private String icon;
    private String description;
    private SysAppStatus status;

    @Override
    public SysAppId identifier() {
        return id;
    }
}
