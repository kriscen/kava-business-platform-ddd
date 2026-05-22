package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysI18nMessageEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysI18nListQuery;
import com.kava.kbpd.upms.domain.model.valobj.SysI18nMessageId;
import com.kava.kbpd.upms.domain.repository.ISysI18nMessageRepository;
import com.kava.kbpd.upms.domain.service.ISysI18nMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysI18nMessageService implements ISysI18nMessageService {
    private final ISysI18nMessageRepository repository;

    @Override
    public SysI18nMessageId create(SysI18nMessageEntity entity) {
        return repository.create(entity);
    }

    @Override
    public Boolean update(SysI18nMessageEntity entity) {
        return repository.update(entity);
    }

    @Override
    public PagingInfo<SysI18nMessageEntity> queryPage(SysI18nListQuery query) {
        return repository.queryPage(query);
    }

    @Override
    public SysI18nMessageEntity queryById(SysI18nMessageId id) {
        return repository.queryById(id);
    }

    @Override
    public Boolean removeBatchByIds(List<SysI18nMessageId> ids) {
        return repository.removeBatchByIds(ids);
    }

    @Override
    public SysI18nMessageEntity queryByCodeAndLanguage(String code, String language) {
        return repository.queryByCodeAndLanguage(code, language);
    }
}
