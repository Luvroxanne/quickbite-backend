package com.itmk.utils;

import com.itmk.constant.SystemConstants;
import com.itmk.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: MK
 * @Description: 用户上下文工具类
 * @Date: 2024/3/20
 */
@Slf4j
public class UserContext {
    /**
     * 获取当前用户ID
     */
    public static Long getCurrentUserId() {
        String token = getTokenFromRequest();
        Long userId = JwtUtil.getUserId(token);
        if (userId == null) {
            throw new BusinessException("未登录或token已过期");
        }
        return userId;
    }

    /**
     * 获取当前用户角色
     */
    public static String getCurrentUserRole() {
        String token = getTokenFromRequest();
        String role = JwtUtil.getUserRole(token);
        if (role == null) {
            throw new BusinessException("未登录或token已过期");
        }
        return role;
    }

    private static String getTokenFromRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith(SystemConstants.TOKEN_PREFIX)) {
            throw new BusinessException("未登录或token已过期");
        }
        return token.replace(SystemConstants.TOKEN_PREFIX, "");
    }
} 