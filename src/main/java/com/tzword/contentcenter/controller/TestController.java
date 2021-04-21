package com.tzword.contentcenter.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.tzword.contentcenter.annotation.CheckAuthorization;
import com.tzword.contentcenter.dao.content.ShareMapper;
import com.tzword.contentcenter.domain.entity.content.Share;
import com.tzword.contentcenter.domain.entity.content.User;
import com.tzword.contentcenter.rocketmq.MySource;
import com.tzword.contentcenter.sentinel.SentinelBlockHandlerClass;
import com.tzword.contentcenter.sentinel.SentinelFallbackClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
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

    @RequestMapping("helloword3")
    @CheckAuthorization(value = "admin")
    public List<Share> printHello2(){
        return shareMapper.selectAll();
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
//        log.info("ribbon调用结果" + forObject);
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


    @GetMapping("/getHotData")
    @SentinelResource("hot")
    public String getHotData(@RequestParam(required = false) String a, @RequestParam(required = false) String b){
        return a+""+b;
    }

    /**
     * @Description: sentinel的API，①Sphu，②Tracer,③ContextUtil
     *
     * ①Sphu让资源受到保护，受到监控
     * ②Tracer可以让我们想要的异常可以被统计
     * ③ContextUtil 可以标记调用的来源
     *
     * @param a 1 
     * @return java.lang.String 
     * @throws
     * @author jianghy
     * @date 2021/4/7 17:08 
     */
    @GetMapping("testSentinelApi")
    public String testSentinelApi(@RequestParam(required = false) String a){
        String resourceName = "test-sentinel-api";
        ContextUtil.enter(resourceName,"test-wfw");
        // 定义一个sentinel保护的资源，名字是：test-sentinel-api
        Entry entry = null;
        try {
            entry = SphU.entry(resourceName);
            if (StringUtils.isBlank(a)) {
                throw new IllegalArgumentException("参数a不能为空");
            }
            // 被保护的业务逻辑
            return a;
        }
        //如果被保护的资源限流或者降级了，就会抛出BlockException异常
        catch (BlockException e) {
            log.info("限流或者降级",e);
            return "限流或者降级";
        }catch (IllegalArgumentException e2){
            //统计IllegalArgumentException发生的占比，次数
            Tracer.trace(e2);
            return "参数非法";
        }
        finally {
            if (entry != null){
                entry.exit();
            }
            ContextUtil.exit();
        }
    }


    /**
     * @Description: SentinelResource来替换上面的api
     * @param a 1
     * @return java.lang.String
     * @throws
     * @author jianghy
     * @date 2021/4/8 13:54
     */
    @GetMapping("testSentinelResource")
    @SentinelResource(value = "test-sentinel-api",
            blockHandler = "block",
            blockHandlerClass = SentinelBlockHandlerClass.class,//限流
            fallback = "fallback",
            fallbackClass = SentinelFallbackClass.class//降级
    )
    public String testSentinelResource(@RequestParam(required = false) String a){
        if (StringUtils.isBlank(a)) {
            throw new IllegalArgumentException("a can not blank");
        }
        // 被保护的业务逻辑
        return a;
    }



    /**
     * @Description: sentinel rest templete
     * @param id 1 
     * @return java.util.List<com.tzword.contentcenter.domain.entity.content.User> 
     * @throws
     * @author jianghy
     * @date 2021/4/8 14:54 
     */
    @GetMapping(value = "sentinel-restTemplete/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserBySentinelRestTemplete(@PathVariable Integer id){
        return this.restTemplate.getForObject("http://user-center/hi/getUser/{id}",User.class,id);
    }


    /**
     * @Description:  使用restTemplete的exchange()方法
     * @param id 1
     * @return java.util.List<com.tzword.contentcenter.domain.entity.content.User>
     * @throws
     * @author jianghy
     * @date 2021/4/8 14:54
     */
    @GetMapping(value = "sentinel-restTemplete-exchange/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserBySentinelRestTempleteExchange(@PathVariable Integer id){
        //先获取request
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = attributes.getRequest();

        //再获取token
        String token = request.getHeader("x-Token");
        //把token放入heads
        HttpHeaders heads = new HttpHeaders();
        heads.add("x-Token",token);

        //进行resttemplate进行请求
        return this.restTemplate.exchange(
                "http://user-center/hi/getUser/{id}",
                HttpMethod.GET,
                new HttpEntity<>(heads),
                User.class,
                id
        );
    }


//    @Autowired
//    private Source source;
//
//    @GetMapping("/test-stream")
//    public String testStream(){
//        this.source.output().send(
//                MessageBuilder.withPayload("消息体").build()
//        );
//        return "success";
//    }
//
//
//    @Autowired
//    private MySource mySource;
//
//    /**
//     * @Description: 自定义的Source
//     * @param  1
//     * @return java.lang.String
//     * @throws
//     * @author jianghy
//     * @date 2021/4/15 21:31
//     */
//    @GetMapping("/test-my-stream")
//    public String testMyStream(){
//        this.mySource.output().send(
//                MessageBuilder.withPayload("消息体").build()
//        );
//        return "success";
//    }

}
