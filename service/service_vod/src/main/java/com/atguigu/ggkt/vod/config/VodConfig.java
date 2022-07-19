package com.atguigu.ggkt.vod.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 配置类
 * @author: 25652
 * @time: 2022/7/13 10:36
 */

@Configuration
@MapperScan("com/atguigu/ggkt/vod/mapper")
public class VodConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor()  {
        return new PaginationInterceptor();
    }
}
