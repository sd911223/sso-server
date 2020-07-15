package com.accurascience.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsConfig implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        //允许所有的域访问
        response.setHeader("Access-Control-Allow-Origin", "*");
        //允许所有方式的请求
        response.setHeader("Access-Control-Allow-Methods", "*");
        //头信息缓存有效时长（如果不设 Chromium 同时规定了一个默认值 5 秒），没有缓存将已OPTIONS进行预请求
        response.setHeader("Access-Control-Max-Age", "3600");
        //允许的头信息
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, authorization");
        //response.setHeader("Content-type", "application/json;charset=utf-8");
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }
}