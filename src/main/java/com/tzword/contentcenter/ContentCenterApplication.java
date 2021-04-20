package com.tzword.contentcenter;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.tzword.contentcenter.configuration.GlobleFeignConfiguration;
import com.tzword.contentcenter.interceptor.TokenRelayTemplateRequestInterceptor;
import com.tzword.contentcenter.rocketmq.MySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.Collections;

@MapperScan("com.tzword.contentcenter.dao.content")
@SpringBootApplication
//@EnableFeignClients(defaultConfiguration = GlobleFeignConfiguration.class)
@EnableFeignClients
@EnableBinding(Source.class)
public class ContentCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentCenterApplication.class, args);
    }

    @Bean
    @LoadBalanced
    @SentinelRestTemplate
    public RestTemplate getRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        //放入多个拦截器
        restTemplate.setInterceptors(
                Collections.singletonList(new TokenRelayTemplateRequestInterceptor())
        );
        return restTemplate;
    }

}
