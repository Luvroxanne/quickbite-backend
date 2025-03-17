package com.itmk.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: MK
 * @Description: JWT拦截器
 * @Date: 2024/3/20
 */
@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 1. 放行登录相关路径
        String path = request.getRequestURI();
        if (path.contains("/auth/") || path.contains("/static/") || 
            path.equals("/") || path.contains(".html")) {
            return true;
        }

        // 2. 放行OPTIONS请求
        if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
            return true;
        }

        // 3. 简单的token验证
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            // 有token就放行
            return true;
        }

        // 4. 未携带token，返回401
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }
} 