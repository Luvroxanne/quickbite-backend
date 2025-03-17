package com.itmk.annotation;

import java.lang.annotation.*;

/**
 * @Author: MK
 * @Description: 角色权限注解
 * @Date: 2024/3/20
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireRole {
    String[] value();
} 