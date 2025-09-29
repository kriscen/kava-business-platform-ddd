package com.kava.kbpd.upms.test;

import com.kava.kbpd.upms.domain.service.ISysAreaService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: test
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UpmsApplicationTest {

    @Resource
    private ISysAreaService sysAreaMapper;

}
