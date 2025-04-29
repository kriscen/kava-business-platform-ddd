package com.kava.kbpd.auth.test;

import com.kava.kbpd.common.cache.redis.RedissonService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Kris
 * @date 2025/4/8
 * @description:
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthApplicationTest {
    @Resource
    private RedissonService redissonService;

    @Test
    public void test1() throws Exception {
//        redissonService.setValue("1234",1234);
        Object value = redissonService.getValue("1234");
        System.out.println(value);
    }

}
