package com.star.webssh.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * myplatisplus中的拦截器
 * 用于增强 myplatisplus
 * 如分页查询这些
 * @author Mr.Wang
 * @create 2023-06-10-23:57
 */
@Configuration
public class MPConfig {
    @Bean
    public MybatisPlusInterceptor pageInterception(){
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }
}
