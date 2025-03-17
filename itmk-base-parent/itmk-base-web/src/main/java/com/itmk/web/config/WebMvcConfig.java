package com.itmk.web.config;

import com.itmk.web.interceptor.JwtInterceptor;
import com.itmk.web.interceptor.RoleInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.beans.factory.annotation.Value;

/**
 * @Author: MK
 * @Description: WebMVC配置类
 * @Date: 2024/3/20
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload.path}")
    private String uploadPath;

    @Override
    public void addInterceptors(@SuppressWarnings("null") InterceptorRegistry registry) {
        // JWT拦截器
        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                    "/api/auth/login",
                    "/api/auth/register",
                    "/api/upload/**"  // 排除文件上传接口
                );
                
        // 角色拦截器
        registry.addInterceptor(new RoleInterceptor())
                .addPathPatterns("/api/**")  // 只拦截/api开头的请求
                .excludePathPatterns(
                    "/api/auth/login",
                    "/api/auth/register"
                );
    }

    @Override
    public void addCorsMappings(@SuppressWarnings("null") CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(@SuppressWarnings("null") ResourceHandlerRegistry registry) {
        // 配置静态资源映射，使上传的文件可以通过URL访问
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath);
    }
} 