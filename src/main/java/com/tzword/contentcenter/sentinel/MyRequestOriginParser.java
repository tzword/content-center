package com.tzword.contentcenter.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jianghy
 * @Description:
 * @date 2021/4/10 10:48
 */
@Component
public class MyRequestOriginParser implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest request) {
        // 从请求种获取参数名为 origin 参数并返回
        // 如果获取不到origin的参数，那么就抛出异常
        // 注意：这个origin可以随便定义，可以定义为aaa

        // 也可以把这个origin参数放在header里，这样，就request.getHeader("origin")

        String origin = request.getParameter("origin");
        if (StringUtils.isBlank(origin)){
            throw new IllegalArgumentException("aaa is must be speslj");
        }
        return origin;
    }
}
