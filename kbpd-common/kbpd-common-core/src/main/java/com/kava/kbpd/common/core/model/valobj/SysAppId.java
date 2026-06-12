package com.kava.kbpd.common.core.model.valobj;

import com.kava.kbpd.common.core.label.Identifier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
public class SysAppId implements Identifier {
    Long id;

    public static SysAppId of(Long id) {
        return id == null ? null : builder().id(id).build();
    }
}
