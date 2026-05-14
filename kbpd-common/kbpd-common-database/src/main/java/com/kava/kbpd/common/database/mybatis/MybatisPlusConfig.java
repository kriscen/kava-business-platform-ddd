package com.kava.kbpd.common.database.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.kava.kbpd.common.database.scope.DataScopeInnerInterceptor;
import com.kava.kbpd.common.database.typeHandler.StringArrayTypeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Set;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: MybatisPlusConfig
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 添加插件：拦截器执行顺序 TenantLine → DataScope → Pagination
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 1. 租户隔离拦截器
        Set<String> ignoreTables = getIgnoreTenantTables();
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(
                new KavaTenantLineInnerInterceptor(ignoreTables)));

        // 2. 数据权限拦截器
        interceptor.addInnerInterceptor(new DataScopeInnerInterceptor());

        // 3. 分页插件（最后添加）
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * 配置忽略租户隔离的表列表，子类可覆盖
     */
    protected Set<String> getIgnoreTenantTables() {
        return Collections.emptySet();
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