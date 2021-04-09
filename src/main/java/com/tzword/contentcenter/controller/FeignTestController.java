package com.tzword.contentcenter.controller;

import com.tzword.contentcenter.domain.entity.content.User;
import com.tzword.contentcenter.feignclient.BaiduFeignClient;
import com.tzword.contentcenter.feignclient.UserCenterFeignClient;
import com.tzword.contentcenter.feignclient.UserCenterFeignParamClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author jianghy
 * @Description:
 * @date 2021/4/5 8:59
 */
@RestController
@RequestMapping("hifeign")
public class FeignTestController {

    @Autowired
    private UserCenterFeignClient userCenterFeignClient;

    @Autowired
    private UserCenterFeignParamClient userCenterFeignParamClient;

    @Autowired
    private BaiduFeignClient baiduFeignClient;

    @GetMapping(value = "getUserByFeign",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getUserByFeign(){
        return userCenterFeignClient.getUserByFeign();
    }

    
    /**
     * @Description: get调用
     * @param user 1 
     * @return java.util.List<com.tzword.contentcenter.domain.entity.content.User> 
     * @throws
     * @author jianghy
     * @date 2021/4/5 21:20
     */
    @GetMapping("getUserByFeignGet")
    public List<User> getUserByGet(@SpringQueryMap User user){
        return userCenterFeignParamClient.getUserByGet(user);
    }

    /**
     * @Description: post调用
     * @param user 1 
     * @return java.util.List<com.tzword.contentcenter.domain.entity.content.User> 
     * @throws
     * @author jianghy
     * @date 2021/4/5 20:26 
     */
    @PostMapping("getUserByFeignPost")
    public List<User> getUserByPost(@RequestBody User user){
        return userCenterFeignParamClient.getUserByPost(user);
    }

    @GetMapping("toBaidu")
    public String getBaidu(){
        return baiduFeignClient.getBaiduIndex();
    }

}
