package com.itmk.service.impl;

import com.itmk.entity.Review;
import com.itmk.entity.Dish;
import com.itmk.entity.Merchant;
import com.itmk.entity.User;
import com.itmk.exception.BusinessException;
import com.itmk.service.ReviewService;
import com.itmk.service.DishService;
import com.itmk.service.MerchantService;
import com.itmk.service.UploadService;
import com.itmk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UploadServiceImpl implements UploadService {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private MerchantService merchantService;
    
    @Autowired
    private DishService dishService;

    @Autowired
    private ReviewService reviewService;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> updateUserAvatar(Long userId, String fileUrl) {
        User user = userService.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setAvatar(fileUrl);
        user.setUpdatedAt(new Date());
        userService.updateById(user);
    
        Map<String, String> result = new HashMap<>();
        result.put("url", fileUrl);
        result.put("avatar", fileUrl);
        return result;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> updateMerchantLogo(Long userId, String fileUrl) {
        Merchant merchant = merchantService.getByUserId(userId);
        if (merchant == null) {
            throw new BusinessException("非商家用户不能上传logo");
        }
        merchant.setLogo(fileUrl);
        merchant.setUpdatedAt(new Date());
        merchantService.updateById(merchant);
    
        Map<String, String> result = new HashMap<>();
        result.put("url", fileUrl);
        result.put("logo", fileUrl);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> uploadDishImage(Long userId, String fileUrl) {
        // 打印接收到的参数
        System.out.println("Received uploadDishImage request - userId: " + userId + ", fileUrl: " + fileUrl);
        
        // 检查是否是商家用户
        Merchant merchant = merchantService.getByUserId(userId);
        if (merchant == null) {
            throw new BusinessException("非商家用户不能上传菜品图片");
        }
        
        // 创建新的菜品记录
        Dish dish = new Dish();

        dish.setImage(fileUrl);
        if( dish.getCreatedAt() == null){
            dish.setCreatedAt(new Date());
        }
        dish.setUpdatedAt(new Date());
        dishService.save(dish);
    
        Map<String, String> result = new HashMap<>();
        result.put("url", fileUrl);
        result.put("dish", fileUrl);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> uploadCommentImage(Long userId, String fileUrl) {
        // 检查用户是否存在
        User user = userService.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 创建新的评论记录
        Review review = new Review();
        review.setCustomerId(userId);
        review.setImages(fileUrl);
        review.setStatus("pending"); // 设置初始状态为待处理
        review.setCreatedAt(new Date());
        review.setUpdatedAt(new Date());
        reviewService.save(review);
    
        Map<String, String> result = new HashMap<>();
        result.put("url", fileUrl);
        result.put("comment", fileUrl);
        return result;
    }
}