package com.accurascience.aspect;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
/**
 * 日志切面
 * @author shitou
 */
public class LogAspect {

    //用来记录请求进入的时间，防止多线程时出错，这里用了ThreadLocal
    ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * 定义切入点，controller下面的所有类的所有公有方法，这里需要更改成自己项目的
     */
    @Pointcut("execution(public * com.accurascience.controller..*.*(..))")
    public void requestLog() {
    }

    ;

    /**
     * 方法之前执行，日志打印请求信息
     *
     * @param joinPoint joinPoint
     */
    @Before("requestLog()")
    public void doBefore(JoinPoint joinPoint) {
        startTime.set(System.currentTimeMillis());
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        //打印当前的请求路径
        Map params = new HashMap();
        // 获取请求的url
        params.put("url", request.getRequestURL());
        // 获取请求的方式
        params.put("method", request.getMethod());
        // 获取请求的ip地址
        params.put("ip", request.getRemoteAddr());
        // 获取类名
        params.put("className", joinPoint.getSignature().getDeclaringTypeName());
        // 获取类方法
        params.put("classMethod", joinPoint.getSignature().getName());
        // 请求参数
        params.put("args", joinPoint.getArgs());


        //这里是从token中获取用户信息，打印当前的访问用户，代码不通用
/*        String token = request.getHeader(JwtUtils.TOKEN_HEADER);
        if (token != null && token.startsWith(JwtUtils.TOKEN_PREFIX)) {
            token = token.replace(JwtUtils.TOKEN_PREFIX, "");
            String username = JwtUtils.getUsername(token);
            log.info("Current User is:[{}]",username);
        }*/

        //打印请求参数，如果需要打印其他的信息可以到request中去拿
        // 输出格式化后的json字符串
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        log.info("REQUEST: {}", gson.toJson(params));
    }

    /**
     * 方法返回之前执行，打印才返回值以及方法消耗时间
     *
     * @param response 返回值
     */
    @AfterReturning(returning = "response", pointcut = "requestLog()")
    public void doAfterRunning(Object response) {
        //打印返回值信息
        log.info("Response:[{}]", response);
        //打印请求耗时
        log.info("Request spend times : [{}ms]", System.currentTimeMillis() - startTime.get());
    }

}
