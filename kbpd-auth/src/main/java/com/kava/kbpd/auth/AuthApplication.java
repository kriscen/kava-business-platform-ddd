package com.kava.kbpd.auth;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Kris
 * @date 2025/4/1
 * @description: auth 启动类
 */
@Slf4j
@EnableDubbo
@SpringBootApplication
public class AuthApplication {
    public static void main(String[] args){
        SpringApplication.run(AuthApplication.class, args);
    }
}
