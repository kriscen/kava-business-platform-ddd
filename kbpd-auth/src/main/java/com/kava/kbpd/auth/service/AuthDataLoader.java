package com.kava.kbpd.auth.service;

import cn.dev33.satoken.oauth2.data.loader.SaOAuth2DataLoader;
import cn.dev33.satoken.oauth2.data.model.loader.SaClientModel;
import cn.hutool.core.util.StrUtil;
import com.kava.kbpd.upms.api.model.dto.SysOauthClientDetailsDTO;
import com.kava.kbpd.upms.api.service.IRemoteOauthClientDetailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Kris
 * @date 2025/4/3
 * @description: client 数据加载
 */
@Slf4j
@Component
public class AuthDataLoader implements SaOAuth2DataLoader {

    @DubboReference(version = "1.0")
    private IRemoteOauthClientDetailService remoteOauthClientService;

    @Override
    public SaClientModel getClientModel(String clientId) {
        SysOauthClientDetailsDTO clientDetails = remoteOauthClientService.findByClientId(clientId);
        SaClientModel saClientModel = new SaClientModel();
        return saClientModel.setClientId(clientDetails.getClientId())
                .setClientSecret(clientDetails.getClientSecret())
                .setContractScopes(List.of(clientDetails.getScope().split(StrUtil.COMMA)))
                .setAllowRedirectUris(List.of(clientDetails.getWebServerRedirectUri().split(StrUtil.COMMA)))
                .setAllowGrantTypes(List.of(clientDetails.getAuthorizedGrantTypes()))
                .setSubjectId(String.valueOf(clientDetails.getTenantId()))
                .setIsNewRefresh(Boolean.FALSE);
    }

    @Override
    public String getOpenid(String clientId, Object loginId) {
        return SaOAuth2DataLoader.super.getOpenid(clientId, loginId);
    }

    @Override
    public String getUnionid(String subjectId, Object loginId) {
        return SaOAuth2DataLoader.super.getUnionid(subjectId, loginId);
    }

}
