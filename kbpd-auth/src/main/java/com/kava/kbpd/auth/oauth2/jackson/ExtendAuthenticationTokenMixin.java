package com.kava.kbpd.auth.oauth2.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeInfo(
    use = Id.CLASS,
    include = As.PROPERTY,
    property = "@class"
)
@JsonAutoDetect(
    fieldVisibility = Visibility.ANY,
    getterVisibility = Visibility.NONE,
    isGetterVisibility = Visibility.NONE
)
@JsonDeserialize(
    using = ExtendAuthenticationTokenDeserializer.class
)
abstract class ExtendAuthenticationTokenMixin {
    ExtendAuthenticationTokenMixin() {
    }
}
