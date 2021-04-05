package com.tzword.contentcenter.configuration;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 * @author jianghy
 * @Description:
 * @date 2021/4/5 10:50
 */
public class GlobleFeignConfiguration {
    @Bean
    public Logger.Level level(){
        return Logger.Level.FULL;
    }
}
