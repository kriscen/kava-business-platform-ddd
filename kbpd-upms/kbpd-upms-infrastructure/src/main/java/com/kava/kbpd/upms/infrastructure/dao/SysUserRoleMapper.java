package com.kava.kbpd.upms.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysUserRolePO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Kris
 * @description: 用户角色关联 Mapper
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRolePO> {
}
