package com.kava.kbpd.upms.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kava.kbpd.upms.infrastructure.dao.po.SysAuditLogPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Kris
 * @date 2025/3/18
 * @description:
 */
@Mapper
public interface SysAuditLogMapper extends BaseMapper<SysAuditLogPO> {
}
