package com.tzword.contentcenter.interceptor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author jianghy
 * @Description:
 * @date 2021/4/20 23:05
 */
public class TokenRelayTemplateRequestInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        //先获取request
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = attributes.getRequest();

        //再获取token
        String token = request.getHeader("x-Token");
        HttpHeaders headers = httpRequest.getHeaders();
        headers.add("x-Token",token);

        //保证继续执行请求，保证其他的拦截器可以正常的执行
        return clientHttpRequestExecution.execute(httpRequest,bytes);




    }
}
