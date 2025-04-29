package com.kava.kbpd.upms.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysAreaListQuery;
import com.kava.kbpd.upms.infrastructure.dao.po.SysAreaPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Kris
 * @date 2025/3/18
 * @description:
 */
@Mapper
public interface SysAreaMapper extends BaseMapper<SysAreaPO> {
}
