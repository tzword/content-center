package com.tzword.contentcenter.annotation;
import com.tzword.contentcenter.util.JwtOperator;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

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

    @Pointcut("@annotation(com.tzword.contentcenter.annotation.CheckAuthorization)")
    public void checkAuthPointCut(){

    }

    /**
     * @Description: 判断token的切面
     * @param joinPoint 1
     * @return java.lang.Object
     * @throws
     * @author jianghy
     * @date 2021/4/21 10:09
     */
    @Around("checkPointCut()")
    public Object checkLogin(ProceedingJoinPoint joinPoint) throws Throwable{
        this.checkToken();
        //joinPoint.process指得是调用原方法
        return joinPoint.proceed();
    }


    /**
     * @Description: 校验token
     * @param  1 
     * @return void 
     * @throws
     * @author jianghy
     * @date 2021/4/21 11:14 
     */
    public void checkToken(){
        try {
            //1.拿到request,获取header里token
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
            HttpServletRequest request = attributes.getRequest();
            String token = request.getHeader("x-Token");

            //2.校验token是否合法或者过期，如果不合法或过期就抛出异常，如果合法就放行
            Boolean aBoolean = jwtOperator.validateToken(token);
            if (!aBoolean){
                log.error("token认证失败");
                throw new SecurityException("token认证失败");
            }
            //3.校验成功，就将用户的信息设置到request里面
            Claims claims = jwtOperator.getClaimsFromToken(token);
            request.setAttribute("id",claims.get("id"));
            request.setAttribute("name",claims.get("name"));
            request.setAttribute("role", claims.get("role"));
        } catch (Throwable throwable) {
            throw new SecurityException("token认证失败",throwable);
        }
    }


    /**
     * @Description: 判断角色的切面
     * @param joinPoint 1 
     * @return java.lang.Object 
     * @throws
     * @author jianghy
     * @date 2021/4/21 10:10 
     */
    @Around("checkAuthPointCut()")
    public Object checkAuth(ProceedingJoinPoint joinPoint) throws Throwable{
        try {
            //1.验证token
            this.checkToken();

            //2.拿到request
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
            HttpServletRequest request = attributes.getRequest();

            //3.拿到注解上的角色值
            Signature signature = joinPoint.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            Method method = methodSignature.getMethod();
            CheckAuthorization annotation = method.getAnnotation(CheckAuthorization.class);
            String value = annotation.value();

            //4.拿到jwt里的角色值跟注解上的比较
            String role = (String)request.getAttribute("role");
            if (!Objects.equals(role,value)){
                throw new SecurityException("用户无权访问");
            }
        } catch (Throwable throwable) {
            throw new SecurityException("用户无权访问",throwable);
        }
        //joinPoint.process指得是调用原方法
        return joinPoint.proceed();
    }


}
