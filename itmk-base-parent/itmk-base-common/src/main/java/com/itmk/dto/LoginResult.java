package com.itmk.dto;

import lombok.Data;

@Data
public class LoginResult {
    /**
     * token令牌
     */
    private String token;
    
    /**
     * 用户信息
     */
    private UserInfo userInfo;
    
    @Data
    public static class UserInfo {
        /**
         * 用户ID
         */
        private Long id;
        
        /**
         * 用户名
         */
        private String name;
        
        /**
         * 角色
         */
        private String role;
        
        /**
         * 头像
         */
        private String avatar;
        
        /**
         * 权限列表
         */
        private String[] permissions;
    }
} 