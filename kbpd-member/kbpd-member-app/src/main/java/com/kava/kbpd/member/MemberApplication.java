package com.kava.kbpd.member;

import com.kava.kbpd.common.security.annotation.EnableResourceServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@EnableDubbo
@EnableResourceServer
@SpringBootApplication
public class MemberApplication {
    public static void main(String[] args){
        SpringApplication.run(MemberApplication.class,args);
    }
}
