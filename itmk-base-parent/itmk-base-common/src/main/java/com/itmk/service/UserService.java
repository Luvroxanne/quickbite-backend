package com.itmk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itmk.dto.LoginRequestDTO;
import com.itmk.dto.RegisterRequestDTO;
import com.itmk.entity.User;

import java.util.List;
import java.util.Map;

/**
 * @Author: MK
 * @Description: 用户服务接口
 * @Date: 2024/3/20
 */
public interface UserService extends IService<User> {
    /**
     * 用户登录
     * @param loginRequest 登录请求参数
     * @return token和用户信息
     */
    Map<String, Object> login(LoginRequestDTO loginRequest);

    /**
     * 用户注册
     * @param registerRequest 注册请求参数
     * @return 是否注册成功
     */
    boolean register(RegisterRequestDTO registerRequest);

    /**
     * 检查用户名是否已存在
     * @param username 用户名
     * @return true表示存在，false表示不存在
     */
    boolean checkUsernameExists(String username);
    
    /**
     * 获取用户列表
     * @param role 角色筛选
     * @param keyword 关键字搜索
     * @return 用户列表
     */
    List<User> getUserList(String role, String keyword);
}