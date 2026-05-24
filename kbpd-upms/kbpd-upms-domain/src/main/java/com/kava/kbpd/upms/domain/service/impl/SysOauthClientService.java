package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysOauthClientEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysOauthClientId;
import com.kava.kbpd.upms.domain.model.valobj.SysOauthClientListQuery;
import com.kava.kbpd.upms.domain.repository.ISysOauthClientRepository;
import com.kava.kbpd.upms.domain.service.ISysOauthClientService;
import com.kava.kbpd.upms.types.exception.UpmsBizException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.kava.kbpd.upms.types.exception.UpmsBizErrorCodeEnum.*;

@Service
@RequiredArgsConstructor
public class SysOauthClientService implements ISysOauthClientService {
    private final ISysOauthClientRepository repository;

    @Override
    public SysOauthClientId create(SysOauthClientEntity entity) {
        validateClientIdUnique(entity.getClientId(), null);
        validateClientSecret(entity.getClientSecret());
        validateTokenValidity(entity.getAccessTokenValidity(), entity.getRefreshTokenValidity());
        validateGrantTypes(entity.getAuthorizedGrantTypes());
        return repository.create(entity);
    }

    @Override
    public Boolean update(SysOauthClientEntity entity) {
        SysOauthClientEntity existing = repository.queryById(entity.getId());
        if (existing != null && !existing.getClientId().equals(entity.getClientId())) {
            validateClientIdUnique(entity.getClientId(), entity.getId());
        }
        validateClientSecret(entity.getClientSecret());
        validateTokenValidity(entity.getAccessTokenValidity(), entity.getRefreshTokenValidity());
        validateGrantTypes(entity.getAuthorizedGrantTypes());
        return repository.update(entity);
    }

    @Override
    public PagingInfo<SysOauthClientEntity> queryPage(SysOauthClientListQuery query) {
        return repository.queryPage(query);
    }

    @Override
    public SysOauthClientEntity queryById(SysOauthClientId id) {
        return repository.queryById(id);
    }

    @Override
    public Boolean removeBatchByIds(List<SysOauthClientId> ids) {
        return repository.removeBatchByIds(ids);
    }

    @Override
    public SysOauthClientEntity queryByClientId(String clientId) {
        return repository.queryByClientId(clientId);
    }

    private void validateClientIdUnique(String clientId, SysOauthClientId excludeId) {
        SysOauthClientEntity existing = repository.queryByClientId(clientId);
        if (existing != null && (excludeId == null || !existing.getId().equals(excludeId))) {
            throw new UpmsBizException(CLIENT_ID_DUPLICATE);
        }
    }

    private void validateClientSecret(String clientSecret) {
        if (!StringUtils.hasText(clientSecret)) {
            throw new UpmsBizException(CLIENT_SECRET_REQUIRED);
        }
    }

    private void validateTokenValidity(Integer accessTokenValidity, Integer refreshTokenValidity) {
        if (accessTokenValidity == null || accessTokenValidity <= 0
                || refreshTokenValidity == null || refreshTokenValidity <= 0) {
            throw new UpmsBizException(CLIENT_TOKEN_VALIDITY_INVALID);
        }
    }

    private void validateGrantTypes(String[] authorizedGrantTypes) {
        if (authorizedGrantTypes == null || authorizedGrantTypes.length == 0) {
            throw new UpmsBizException(CLIENT_GRANT_TYPES_REQUIRED);
        }
    }
}
