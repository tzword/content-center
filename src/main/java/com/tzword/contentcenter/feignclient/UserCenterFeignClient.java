package com.tzword.contentcenter.feignclient;

import com.tzword.contentcenter.domain.entity.content.User;
import com.tzword.contentcenter.fallback.UserCenterFeignClientFallback;
import com.tzword.contentcenter.fallback.UserCenterFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author jianghy
 * @Description:
 * @date 2021/4/5 9:23
 */
//@FeignClient(name = "user-center",configuration = UserCenterFeignConfiguration.class)
@FeignClient(name = "user-center",fallbackFactory = UserCenterFeignClientFallbackFactory.class)
//@FeignClient(name = "user-center",fallback = UserCenterFeignClientFallback.class)
//可以在里面假如fallback或者fallbackFactory，这两种只能选择一个，互相排斥
//fallback只能返回被流控后的方法，如果fallbackFactory可以打印被流控后的异常日志
public interface UserCenterFeignClient {

    @GetMapping(value = "/hi/helloword")
    List<User> getUserByFeign();
}
