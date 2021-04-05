package com.tzword.contentcenter.feignclient;

import com.tzword.contentcenter.domain.entity.content.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author jianghy
 * @Description:
 * @date 2021/4/5 18:52
 */
@FeignClient(name = "user-center")
public interface UserCenterFeignParamClient {
    @PostMapping("/hi/getUserByPost")
    List<User> getUserByPost(@RequestBody User user);

    @GetMapping("/hi/getUserByGet")
    List<User> getUserByGet(@SpringQueryMap User user);

}
