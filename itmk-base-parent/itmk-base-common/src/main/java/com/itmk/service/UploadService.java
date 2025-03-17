package com.itmk.service;

import java.util.Map;

public interface UploadService {
    /**
     * 更新用户头像
     */
    Map<String, String> updateUserAvatar(Long userId, String fileUrl);

    /**
     * 更新商家logo
     */
    Map<String, String> updateMerchantLogo(Long userId, String fileUrl);
    
    /**
     * 上传菜品图片
     */
    Map<String, String> uploadDishImage(Long userId, String fileUrl);
    
    /**
     * 上传评论图片
     */
    Map<String, String> uploadCommentImage(Long userId, String fileUrl);
}