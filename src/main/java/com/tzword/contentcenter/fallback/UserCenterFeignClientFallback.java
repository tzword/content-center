package com.tzword.contentcenter.fallback;

import com.tzword.contentcenter.domain.entity.content.User;
import com.tzword.contentcenter.feignclient.UserCenterFeignClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jianghy
 * @Description:
 * @date 2021/4/9 10:33
 */
@Component
public class UserCenterFeignClientFallback implements UserCenterFeignClient {
    @Override
    public List<User> getUserByFeign() {
        List<User> list = new ArrayList<>();
        User user = new User();
        user.setName("李四啊");
        list.add(user);
        return list;
    }
}
