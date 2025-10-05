package com.kava.kbpd.common.database.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.kava.kbpd.common.database.typeHandler.StringArrayTypeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: MybatisPlusConfig
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 添加分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL)); // 如果配置多个插件, 切记分页最后添加
        // 如果有多数据源可以不配具体类型, 否则都建议配上具体的 DbType
        return interceptor;
    }

    /**
     * 添加自定义类型处理器
     */
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> {
            // 注册 TypeHandler
            configuration.getTypeHandlerRegistry().register(String[].class, StringArrayTypeHandler.class);
        };
    }
}