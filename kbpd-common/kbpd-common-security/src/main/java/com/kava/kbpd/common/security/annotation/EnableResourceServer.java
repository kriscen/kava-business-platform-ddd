package com.kava.kbpd.common.security.annotation;

import com.kava.kbpd.common.security.config.MyStpInterface;
import com.kava.kbpd.common.security.config.ResourceServerConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Kris
 * @date 2025/4/9
 * @description: 资源服务器注解
 */
@Documented
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({MyStpInterface.class, ResourceServerConfiguration.class})
public @interface EnableResourceServer {

}