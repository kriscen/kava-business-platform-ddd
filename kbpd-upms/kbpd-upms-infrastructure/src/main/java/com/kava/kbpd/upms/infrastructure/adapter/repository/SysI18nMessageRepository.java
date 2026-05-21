package com.kava.kbpd.upms.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysI18nMessageEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysI18nListQuery;
import com.kava.kbpd.upms.domain.model.valobj.SysI18nMessageId;
import com.kava.kbpd.upms.domain.repository.ISysI18nMessageRepository;
import com.kava.kbpd.upms.infrastructure.converter.SysI18nMessageConverter;
import com.kava.kbpd.upms.infrastructure.dao.SysI18nMessageMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysI18nMessagePO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SysI18nMessageRepository implements ISysI18nMessageRepository {

    private final SysI18nMessageMapper sysI18nMessageMapper;
    private final SysI18nMessageConverter sysI18nMessageConverter;

    @Override
    public SysI18nMessageId create(SysI18nMessageEntity entity) {
        SysI18nMessagePO po = sysI18nMessageConverter.convertEntity2PO(entity);
        sysI18nMessageMapper.insert(po);
        return SysI18nMessageId.builder()
                .id(po.getId())
                .build();
    }

    @Override
    public Boolean update(SysI18nMessageEntity entity) {
        SysI18nMessagePO po = sysI18nMessageConverter.convertEntity2PO(entity);
        return SqlHelper.retBool(sysI18nMessageMapper.updateById(po));
    }

    @Override
    public PagingInfo<SysI18nMessageEntity> queryPage(SysI18nListQuery query) {
        LambdaQueryWrapper<SysI18nMessagePO> wrapper = Wrappers.lambdaQuery(SysI18nMessagePO.class)
                .like(query.getCode() != null, SysI18nMessagePO::getCode, query.getCode())
                .eq(query.getLanguage() != null, SysI18nMessagePO::getLanguage, query.getLanguage());
        Page<SysI18nMessagePO> page = sysI18nMessageMapper.selectPage(
                Page.of(query.getQueryParam().getPageNo(), query.getQueryParam().getPageSize()),
                wrapper);
        return PagingInfo.toResponse(page.getRecords().stream()
                        .map(sysI18nMessageConverter::convertPO2Entity).toList(),
                page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public SysI18nMessageEntity queryById(SysI18nMessageId id) {
        SysI18nMessagePO po = sysI18nMessageMapper.selectById(id.getId());
        return sysI18nMessageConverter.convertPO2Entity(po);
    }

    @Override
    public Boolean removeBatchByIds(List<SysI18nMessageId> ids) {
        List<Long> idList = ids.stream().map(SysI18nMessageId::getId).toList();
        return SqlHelper.retBool(sysI18nMessageMapper.deleteByIds(idList));
    }

    @Override
    public SysI18nMessageEntity queryByCodeAndLanguage(String code, String language) {
        LambdaQueryWrapper<SysI18nMessagePO> wrapper = Wrappers.lambdaQuery(SysI18nMessagePO.class)
                .eq(SysI18nMessagePO::getCode, code)
                .eq(SysI18nMessagePO::getLanguage, language);
        SysI18nMessagePO po = sysI18nMessageMapper.selectOne(wrapper);
        return sysI18nMessageConverter.convertPO2Entity(po);
    }
}
