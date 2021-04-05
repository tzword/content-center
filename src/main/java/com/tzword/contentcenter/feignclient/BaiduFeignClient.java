package com.tzword.contentcenter.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author jianghy
 * @Description:
 * @date 2021/4/5 21:19
 */
@FeignClient(name = "baidu",url = "http://www.baidu.com")
public interface BaiduFeignClient {
    @GetMapping("")
    String getBaiduIndex();
}
