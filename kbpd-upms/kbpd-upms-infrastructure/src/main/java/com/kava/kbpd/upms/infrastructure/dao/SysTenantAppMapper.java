package com.kava.kbpd.upms.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysTenantAppPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysTenantAppMapper extends BaseMapper<SysTenantAppPO> {
}
