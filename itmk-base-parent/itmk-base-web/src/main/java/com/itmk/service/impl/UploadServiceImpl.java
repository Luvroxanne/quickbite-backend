package com.itmk.service.impl;

import com.itmk.service.UploadService;
import com.itmk.service.UserService;
import com.itmk.service.MerchantService;
import com.itmk.entity.User;
import com.itmk.entity.Merchant;
import com.itmk.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class UploadServiceImpl implements UploadService {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private MerchantService merchantService;
    
    @Override
    public Map<String, String> uploadDishImage(Long userId, String fileUrl) {
        Map<String, String> result = new HashMap<>();
        result.put("url", fileUrl);
        result.put("dishImage", fileUrl);
        return result;
    }
    
    @Override
    public Map<String, String> uploadCommentImage(Long userId, String fileUrl) {
        Map<String, String> result = new HashMap<>();
        result.put("url", fileUrl);
        result.put("commentImage", fileUrl);
        return result;
    }
    
    @Override
    public Map<String, String> updateUserAvatar(Long userId, String fileUrl) {
        // 只更新用户头像
        User user = userService.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setAvatar(fileUrl);
        userService.updateById(user);
        
        Map<String, String> result = new HashMap<>();
        result.put("url", fileUrl);
        return result;
    }
    
    @Override
    public Map<String, String> updateMerchantLogo(Long userId, String fileUrl) {
        // 只更新商家logo
        Merchant merchant = merchantService.getByUserId(userId);
        if (merchant == null) {
            throw new BusinessException("商家信息不存在");
        }
        merchant.setLogo(fileUrl);
        merchantService.updateById(merchant);
        
        Map<String, String> result = new HashMap<>();
        result.put("url", fileUrl);
        return result;
    }
}