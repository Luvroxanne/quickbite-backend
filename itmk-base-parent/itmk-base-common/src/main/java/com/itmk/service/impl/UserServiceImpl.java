package com.itmk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itmk.dto.LoginRequestDTO;
import com.itmk.dto.RegisterRequestDTO;
import com.itmk.entity.Merchant;
import com.itmk.entity.User;
import com.itmk.exception.BusinessException;
import com.itmk.mapper.UserMapper;
import com.itmk.service.MerchantService;
import com.itmk.service.UserService;
import com.itmk.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: MK
 * @Description: 用户服务实现类
 * @Date: 2024/3/20
 */
@Service
@Slf4j  // 添加日志注解
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private MerchantService merchantService;

    

    @Override
    public Map<String, Object> login(LoginRequestDTO loginRequest) {
        log.info("开始处理登录请求: {}", loginRequest);
        
        // 1. 根据用户名查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginRequest.getUsername());
        User user = this.getOne(queryWrapper);
        
        log.info("查询到的用户信息: {}", user);
        
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 2. 校验密码
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            throw new BusinessException("密码错误");
        }

        // 3. 生成token
        String token = JwtUtil.generateToken(user.getId().toString(), user.getRole());
        
        // 4. 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);
        
        log.info("登录成功，返回结果: {}", result);
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean register(RegisterRequestDTO registerRequest) {
        // 1. 校验用户名是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", registerRequest.getUsername());
        if (this.count(queryWrapper) > 0) {
            throw new BusinessException("用户名已存在");
        }

        // 2. 创建用户 (使用明文密码)
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());  // 直接使用明文密码
        user.setRole(registerRequest.getUserType());
        user.setPhone(registerRequest.getPhone());
        user.setEmail(registerRequest.getEmail());
        
        boolean success = this.save(user);

        // 3. 如果是商家，创建商家信息
        if (success && "merchant".equals(registerRequest.getUserType())) {
            Merchant merchant = new Merchant();
            merchant.setUserId(user.getId());
            merchant.setStoreName(registerRequest.getStoreName());
            merchant.setAddress(registerRequest.getAddress());
            merchant.setStatus(1);
            merchant.setRating(5.0);
            success = merchantService.createMerchant(merchant);
        }

        return success;
    }

    @Override
    public boolean checkUsernameExists(String username) {
        if (!StringUtils.hasText(username)) {
            return false;
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return this.count(queryWrapper) > 0;
    }

    @Override
    public List<User> getUserList(String role, String keyword) {
        // 构建查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        
        // 只查询merchant、customer、rider三种角色
        queryWrapper.in("role", "merchant", "customer", "rider");
        
        // 按角色筛选
        if (StringUtils.hasText(role)) {
            queryWrapper.eq("role", role);
        }
        
        // 关键字搜索（用户名、手机号、邮箱）
        if (StringUtils.hasText(keyword)) {
            queryWrapper.and(wrapper -> wrapper
                .like("username", keyword)
                .or()
                .like("phone", keyword)
                .or()
                .like("email", keyword)
            );
        }
        
        // 按创建时间降序排序
        queryWrapper.orderByDesc("created_at");
        
        // 查询列表
        return this.list(queryWrapper);
    }
}