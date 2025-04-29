package com.kava.kbpd.auth.oauth2.jackson;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class OAuth2AuthorizationConsentMixin {
    @JsonCreator
    OAuth2AuthorizationConsentMixin(@JsonProperty("registeredClientId") String registeredClientId,
                                    @JsonProperty("principalName") String principalName,
                                    @JsonProperty("authorities") Set<GrantedAuthority> authorities) {
    }
}
