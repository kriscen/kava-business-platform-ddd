package com.kava.kbpd.auth.oauth2.jackson;

import com.fasterxml.jackson.annotation.*;

import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class OAuth2AuthorizationTokenMixin {
    @JsonCreator
    OAuth2AuthorizationTokenMixin(@JsonProperty("token") String token,
                                  @JsonProperty("metadata") Map<String, Object> metadata) {
    }
}
