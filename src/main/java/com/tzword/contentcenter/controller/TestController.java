package com.tzword.contentcenter.controller;

import com.tzword.contentcenter.dao.content.ShareMapper;
import com.tzword.contentcenter.domain.entity.content.Share;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author jianghy
 * @Description: 测试
 * @date 2021/3/29 16:08
 */
@RestController
@RequestMapping("hi")
@Slf4j
public class TestController {
    @Autowired
    private ShareMapper shareMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;


    @RequestMapping("helloword2")
    public void printHello(){
        List<Share> users = shareMapper.selectAll();
        System.out.println(users.toString());
    }

    @GetMapping("diaoyong")
    public void getUser(){
        List<ServiceInstance> instances = discoveryClient.getInstances("user-center");
//        String url = instances.stream().map(instance -> instance.getUri().toASCIIString() + "/hi/helloword").findFirst().orElseThrow(() -> new IllegalArgumentException("当前没有实例"));
        List<String> targetsUrls = instances.stream().map(instance -> instance.getUri().toASCIIString() + "/hi/helloword").collect(Collectors.toList());
        int i = ThreadLocalRandom.current().nextInt(targetsUrls.size());

//        ResponseEntity<String> forEntity = restTemplate.getForEntity("http://localhost:8080/hi/helloword", String.class);
//        log.info(forEntity.getBody());
        log.info("请求的地址："+ targetsUrls.get(i));
        String forObject = restTemplate.getForObject(targetsUrls.get(i), String.class);
        log.info(forObject);
    }

    @GetMapping("getUserByRibbon")
    public void getUserByRibbon(){
        String forObject = restTemplate.getForObject("http://user-center/hi/helloword", String.class);
        log.info("ribbon调用结果" + forObject);
    }


    /**
     * @Description: 测试指定服务所有的实例信息
     * @param  1 
     * @return java.util.List<org.springframework.cloud.client.ServiceInstance> 
     * @throws
     * @author jianghy
     * @date 2021/3/31 14:24 
     */
    @GetMapping("getInstance")
    public List<ServiceInstance> getInstance(){
        List<ServiceInstance> instances = discoveryClient.getInstances("user-center");
        return instances;
    }
    
    /**
     * @Description: 测试有哪些微服务
     * @param  1 
     * @return java.util.List<java.lang.String> 
     * @throws
     * @author jianghy
     * @date 2021/3/31 14:25 
     */
    @GetMapping("getServices")
    public List<String> getServices(){
        List<String> services = discoveryClient.getServices();
        return services;
    }
    
    
    


}
