package com.kava.kbpd.upms.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysAppPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysAppMapper extends BaseMapper<SysAppPO> {
}
