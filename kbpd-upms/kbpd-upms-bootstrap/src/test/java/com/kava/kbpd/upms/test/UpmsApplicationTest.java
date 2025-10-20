package com.kava.kbpd.upms.test;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private PasswordEncoder passwordEncoder;

    @Test
    public void testPwd() {
        String password = passwordEncoder.encode("123456");
        System.out.println(password);
    }

}
