package com.tzword.contentcenter.annotation;
import com.tzword.contentcenter.exception.MySecurityException;
import com.tzword.contentcenter.util.JwtOperator;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jianghy
 * @Description:
 * @date 2021/4/19 20:38
 */
@Aspect
@Component
@Slf4j
public class CheckAspect {

    @Autowired
    private JwtOperator jwtOperator;

    @Pointcut("@annotation(com.tzword.contentcenter.annotation.CheckLogin)")
    public void checkPointCut(){

    }

    @Around("checkPointCut()")
    public Object before(ProceedingJoinPoint joinPoint){
        try {
            //拿到request,获取header里token
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
            HttpServletRequest request = attributes.getRequest();
            String token = request.getHeader("x-Token");

            //        Signature signature = joinPoint.getSignature();
            //        MethodSignature methodSignature = (MethodSignature) signature;
            //        Method method = methodSignature.getMethod();
            //        CheckLogin annotation = method.getAnnotation(CheckLogin.class);
            //校验token是否合法或者过期，如果不合法或过期就抛出异常，如果合法就放行
            Boolean aBoolean = jwtOperator.validateToken(token);
            if (!aBoolean){
                log.error("token认证失败");
                throw new MySecurityException();
            }
            //joinPoint.process指得是调用原方法
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            throw new MySecurityException();
        }
    }


}
