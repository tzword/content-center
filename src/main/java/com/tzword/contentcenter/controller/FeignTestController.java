package com.tzword.contentcenter.controller;

import com.tzword.contentcenter.feignclient.UserCenterFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("getUserByFeign")
    public void getUserByFeign(){
        userCenterFeignClient.getUserByFeign();
    }
}
