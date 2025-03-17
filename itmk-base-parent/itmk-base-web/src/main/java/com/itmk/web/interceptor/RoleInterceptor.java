package com.itmk.web.interceptor;

import com.itmk.annotation.RequireRole;
import com.itmk.exception.BusinessException;
import com.itmk.utils.UserContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Author: MK
 * @Description: 角色权限拦截器
 * @Date: 2024/3/20
 */
public class RoleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 获取类上的注解
        RequireRole classAnnotation = handlerMethod.getBeanType().getAnnotation(RequireRole.class);
        // 获取方法上的注解
        RequireRole methodAnnotation = handlerMethod.getMethodAnnotation(RequireRole.class);

        // 如果都没有注解，则不需要权限
        if (classAnnotation == null && methodAnnotation == null) {
            return true;
        }

        // 获取当前用户角色
        String userRole = UserContext.getCurrentUserRole();
        
        // 优先判断方法上的注解
        if (methodAnnotation != null) {
            return checkRole(methodAnnotation.value(), userRole);
        }
        
        // 判断类上的注解
        return checkRole(classAnnotation.value(), userRole);
    }

    /**
     * 检查角色权限
     */
    private boolean checkRole(String[] allowRoles, String userRole) {
        boolean hasPermission = Arrays.asList(allowRoles).contains(userRole);
        if (!hasPermission) {
            throw new BusinessException("没有权限访问该接口");
        }
        return true;
    }
} 