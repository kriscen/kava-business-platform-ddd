package com.kava.kbpd.upms.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysUserPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Kris
 * @date 2025/3/18
 * @description:
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUserPO> {
}
