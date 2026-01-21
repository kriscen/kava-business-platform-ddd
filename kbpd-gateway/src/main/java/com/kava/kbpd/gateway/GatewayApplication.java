package com.kava.kbpd.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Kris
 * @date 2025/3/31
 * @description: gateway 启动类
 */
@Slf4j
@SpringBootApplication
public class GatewayApplication {
    public static void main(String[] args){
        SpringApplication.run(GatewayApplication.class, args);
    }
}
