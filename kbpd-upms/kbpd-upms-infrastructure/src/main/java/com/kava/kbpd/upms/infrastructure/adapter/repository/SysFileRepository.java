package com.kava.kbpd.upms.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysFileEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysFileId;
import com.kava.kbpd.upms.domain.model.valobj.SysFileListQuery;
import com.kava.kbpd.upms.domain.repository.ISysFileRepository;
import com.kava.kbpd.upms.infrastructure.converter.SysFileConverter;
import com.kava.kbpd.upms.infrastructure.dao.SysFileMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysFilePO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysFileRepository implements ISysFileRepository {

    @Resource
    private SysFileMapper sysFileMapper;
    @Resource
    private SysFileConverter sysFileConverter;
    @Override
    public SysFileId create(SysFileEntity entity) {
        SysFilePO sysFilePO = sysFileConverter.convertEntity2PO(entity);
        sysFileMapper.insert(sysFilePO);
        return SysFileId.builder()
                .id(sysFilePO.getId())
                .build();
    }

    @Override
    public Boolean update(SysFileEntity entity) {
        SysFilePO sysFilePO = sysFileConverter.convertEntity2PO(entity);
        return SqlHelper.retBool(sysFileMapper.updateById(sysFilePO));
    }

    @Override
    public PagingInfo<SysFileEntity> queryPage(SysFileListQuery query) {
        Page<SysFilePO> sysFilePOPage = sysFileMapper.selectPage(
                Page.of(query.getQueryParam().getPageNo(), query.getQueryParam().getPageSize()),
                Wrappers.lambdaQuery(SysFilePO.class));
        return PagingInfo.toResponse(sysFilePOPage.getRecords().stream()
                        .map(sysFileConverter::convertPO2Entity).toList(),
                sysFilePOPage.getTotal(), sysFilePOPage.getCurrent(), sysFilePOPage.getSize());
    }

    @Override
    public SysFileEntity queryById(SysFileId id) {
        SysFilePO sysFilePO = sysFileMapper.selectById(id.getId());
        return sysFileConverter.convertPO2Entity(sysFilePO);
    }

    @Override
    public Boolean removeBatchByIds(List<SysFileId> ids) {
        List<Long> idList = ids.stream().map(SysFileId::getId).toList();
        return SqlHelper.retBool(sysFileMapper.deleteByIds(idList));
    }
}