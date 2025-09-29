package com.kava.kbpd.upms.domain.service.impl;

import com.kava.kbpd.upms.domain.repository.ISysUserReadRepository;
import com.kava.kbpd.upms.domain.repository.ISysUserWriteRepository;
import com.kava.kbpd.upms.domain.service.ISysUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class SysUserService implements ISysUserService {
    @Resource
    private ISysUserWriteRepository writeRepository;

    @Resource
    private ISysUserReadRepository readRepository;


}