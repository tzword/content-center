package com.tzword.contentcenter.fallback;

import com.tzword.contentcenter.domain.entity.content.User;
import com.tzword.contentcenter.feignclient.UserCenterFeignClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jianghy
 * @Description:
 * @date 2021/4/9 10:48
 */
@Component
@Slf4j
public class UserCenterFeignClientFallbackFactory implements FallbackFactory<UserCenterFeignClient> {
    @Override
    public UserCenterFeignClient create(Throwable throwable) {
        return () -> {
            List<User> list = new ArrayList<>();
            User user = new User();
            user.setName("李四啊");
            list.add(user);
            log.info("好难受，被限流了呢",throwable);
            return list;
        };
    }
}
