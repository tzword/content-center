package com.tzword.contentcenter.controller;

import com.tzword.contentcenter.domain.entity.content.User;
import com.tzword.contentcenter.feignclient.UserCenterFeignClient;
import com.tzword.contentcenter.feignclient.UserCenterFeignParamClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
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

    @GetMapping("getUserByFeign")
    public void getUserByFeign(){
        userCenterFeignClient.getUserByFeign();
    }

    
    /**
     * @Description: get调用
     * @param user 1 
     * @return java.util.List<com.tzword.contentcenter.domain.entity.content.User> 
     * @throws
     * @author jianghy
     * @date 2021/4/5 20:27 
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

}
