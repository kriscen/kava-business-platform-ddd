package com.kava.kbpd.common.security.annotations;

import com.kava.kbpd.common.security.config.ResourceServerConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Inherited
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Import({ ResourceServerConfiguration.class,})
public @interface EnableResourceServer {

}