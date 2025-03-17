package com.itmk.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @Author: MK
 * @Description: JWT工具类
 * @Date: 2024/3/20
 */
@Slf4j
public class JwtUtil {
    // 使用简单的密钥
    private static final String SECRET = "itmk-restaurant-management-system-jwt-secret-key-2000";
    
    // token过期时间 7天
    private static final long EXPIRATION = 604800L;

    /**
     * 生成token
     */
    public static String generateToken(String userId, String role) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION * 1000);
        
        return Jwts.builder()
                .setSubject(userId)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, SECRET.getBytes())
                .compact();
    }

    /**
     * 解析token
     */
    public static Claims parseToken(String token) {
        if (!StringUtils.hasText(token)) {
            return null;
        }
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET.getBytes())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("解析token失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从token中获取用户ID
     */
    public static Long getUserId(String token) {
        Claims claims = parseToken(token);
        if (claims != null) {
            return Long.parseLong(claims.getSubject());
        }
        return null;
    }

    /**
     * 从token中获取用户角色
     */
    public static String getUserRole(String token) {
        Claims claims = parseToken(token);
        return claims != null ? (String) claims.get("role") : null;
    }

    /**
     * 验证token是否过期
     */
    public static boolean isTokenExpired(String token) {
        Claims claims = parseToken(token);
        return claims == null || claims.getExpiration().before(new Date());
    }
} 