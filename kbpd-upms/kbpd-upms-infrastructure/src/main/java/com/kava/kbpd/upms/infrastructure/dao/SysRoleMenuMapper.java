package com.kava.kbpd.upms.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysRoleMenuPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Kris
 * @description: 角色菜单关联 Mapper
 */
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenuPO> {
}
