package com.itmk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itmk.entity.Notification;
import com.itmk.dto.MarkReadDTO;
import java.util.List;

public interface NotificationService extends IService<Notification> {
    
    /**
     * 获取用户通知列表
     */
    List<Notification> getUserNotifications(Long userId);
    
    /**
     * 标记通知为已读
     */
    boolean markAsRead(MarkReadDTO markReadDTO, Long userId);
    
    /**
     * 创建订单通知
     */
    void createOrderNotification(Long userId, String orderNo, String status);
    
    /**
     * 创建库存预警通知
     */
    void createInventoryWarningNotification(Long userId, String itemName, Integer stock);
    
    /**
     * 创建支付成功通知
     */
    void createPaymentNotification(Long userId, String orderNo, String amount);
    
    /**
     * 创建订单配送通知
     */
    void createDeliveryNotification(Long userId, String orderNo, String status);
} 