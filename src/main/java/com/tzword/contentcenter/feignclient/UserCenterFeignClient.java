package com.tzword.contentcenter.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author jianghy
 * @Description:
 * @date 2021/4/5 9:23
 */
//@FeignClient(name = "user-center",configuration = UserCenterFeignConfiguration.class)
@FeignClient(name = "user-center")
public interface UserCenterFeignClient {

    @GetMapping("/hi/helloword")
    Object getUserByFeign();
}
