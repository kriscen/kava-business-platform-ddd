package com.kava.kbpd.upms.domain.model.valobj;

import com.kava.kbpd.common.core.base.QueryParamValObj;
import com.kava.kbpd.common.core.label.ValueObject;
import lombok.*;

import static lombok.AccessLevel.PRIVATE;

@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
public class SysAppListQuery implements ValueObject {
    QueryParamValObj queryParam;
    String appName;
}
