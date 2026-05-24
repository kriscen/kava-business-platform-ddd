package com.kava.kbpd.upms.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.domain.model.entity.SysDeptEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysDeptId;
import com.kava.kbpd.upms.domain.model.valobj.SysDeptListQuery;
import com.kava.kbpd.upms.domain.repository.ISysDeptRepository;
import com.kava.kbpd.upms.infrastructure.converter.SysDeptConverter;
import com.kava.kbpd.upms.infrastructure.dao.SysDeptMapper;
import com.kava.kbpd.upms.infrastructure.dao.SysUserMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysDeptPO;
import com.kava.kbpd.upms.infrastructure.dao.po.SysUserPO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysDeptRepository implements ISysDeptRepository {

    @Resource
    private SysDeptMapper sysDeptMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysDeptConverter sysDeptConverter;

    @Override
    public SysDeptId create(SysDeptEntity entity) {
        SysDeptPO sysDeptPO = sysDeptConverter.convertEntity2PO(entity);
        sysDeptMapper.insert(sysDeptPO);
        return SysDeptId.builder()
                .id(sysDeptPO.getId())
                .build();
    }

    @Override
    public Boolean update(SysDeptEntity entity) {
        SysDeptPO sysDeptPO = sysDeptConverter.convertEntity2PO(entity);
        return SqlHelper.retBool(sysDeptMapper.updateById(sysDeptPO));
    }

    @Override
    public PagingInfo<SysDeptEntity> queryPage(SysDeptListQuery query) {
        Page<SysDeptPO> sysDeptPOPage = sysDeptMapper.selectPage(
                Page.of(query.getQueryParam().getPageNo(), query.getQueryParam().getPageSize()),
                Wrappers.lambdaQuery(SysDeptPO.class)
                        .like(query.getDeptName() != null, SysDeptPO::getName, query.getDeptName()));
        return PagingInfo.toResponse(sysDeptPOPage.getRecords().stream()
                        .map(sysDeptConverter::convertPO2Entity).toList(),
                sysDeptPOPage.getTotal(), sysDeptPOPage.getCurrent(), sysDeptPOPage.getSize());
    }

    @Override
    public SysDeptEntity queryById(SysDeptId id) {
        SysDeptPO sysDeptPO = sysDeptMapper.selectById(id.getId());
        return sysDeptConverter.convertPO2Entity(sysDeptPO);
    }

    @Override
    public Boolean removeBatchByIds(List<SysDeptId> ids) {
        List<Long> idList = ids.stream().map(SysDeptId::getId).toList();
        return SqlHelper.retBool(sysDeptMapper.deleteByIds(idList));
    }

    @Override
    public List<SysDeptEntity> queryAll() {
        return sysDeptMapper.selectList(Wrappers.lambdaQuery(SysDeptPO.class))
                .stream()
                .map(sysDeptConverter::convertPO2Entity)
                .toList();
    }

    @Override
    public List<SysDeptEntity> queryByIds(List<SysDeptId> ids) {
        List<Long> idList = ids.stream().map(SysDeptId::getId).toList();
        if (idList.isEmpty()) {
            return List.of();
        }
        return sysDeptMapper.selectBatchIds(idList)
                .stream()
                .map(sysDeptConverter::convertPO2Entity)
                .toList();
    }

    @Override
    public List<SysDeptEntity> queryByPid(SysDeptId pid) {
        return sysDeptMapper.selectList(
                        Wrappers.lambdaQuery(SysDeptPO.class)
                                .eq(SysDeptPO::getPid, pid.getId()))
                .stream()
                .map(sysDeptConverter::convertPO2Entity)
                .toList();
    }

    @Override
    public boolean existsUserReference(SysDeptId deptId) {
        return sysUserMapper.selectCount(
                        Wrappers.lambdaQuery(SysUserPO.class)
                                .eq(SysUserPO::getDeptId, deptId.getId()))
                > 0;
    }
}
