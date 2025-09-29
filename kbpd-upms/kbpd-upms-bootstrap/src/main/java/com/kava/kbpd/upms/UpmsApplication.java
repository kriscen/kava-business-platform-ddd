package com.kava.kbpd.upms;

import com.kava.kbpd.common.security.annotations.EnableResourceServer;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@SpringBootApplication
@EnableResourceServer
public class UpmsApplication {
    public static void main(String[] args){
        SpringApplication.run(UpmsApplication.class,args);
    }
}
