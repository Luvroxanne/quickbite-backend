package com.itmk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itmk.entity.Notification;
import com.itmk.mapper.NotificationMapper;
import com.itmk.service.NotificationService;
import com.itmk.dto.MarkReadDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {
    
    @Override
    public List<Notification> getUserNotifications(Long userId) {
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getUserId, userId)
                .eq(Notification::getIsRead, 0)  // 只查询未读通知
                .orderByDesc(Notification::getCreatedAt);
        return this.list(wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean markAsRead(MarkReadDTO markReadDTO, Long userId) {
        try {
            if (markReadDTO.getId() == null) {
                // 标记所有通知为已读
                return this.lambdaUpdate()
                        .set(Notification::getIsRead, 1)
                        .set(Notification::getUpdatedAt, new Date())
                        .eq(Notification::getUserId, userId)
                        .eq(Notification::getIsRead, 0)
                        .update();
            } else {
                // 标记单个通知为已读
                return this.lambdaUpdate()
                        .set(Notification::getIsRead, 1)
                        .set(Notification::getUpdatedAt, new Date())
                        .eq(Notification::getUserId, userId)
                        .eq(Notification::getId, markReadDTO.getId())
                        .update();
            }
        } catch (Exception e) {
            log.error("标记通知已读失败", e);
            return false;
        }
    }  

    @Override
    public void createOrderNotification(Long userId, String orderNo, String status) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType("order_notice");
        notification.setTitle("订单状态更新");
        notification.setContent("您的订单 " + orderNo + " 状态已更新为：" + status);
        notification.setIsRead(0);
        notification.setCreatedAt(new Date());
        notification.setUpdatedAt(new Date());
        this.save(notification);
    }

    @Override
    public void createInventoryWarningNotification(Long userId, String itemName, Integer stock) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType("inventory_notice");
        notification.setTitle("库存预警");
        notification.setContent("商品 " + itemName + " 当前库存为 " + stock + "，已达到预警值");
        notification.setIsRead(0);
        notification.setCreatedAt(new Date());
        notification.setUpdatedAt(new Date());
        this.save(notification);
    }

    @Override
    public void createPaymentNotification(Long userId, String orderNo, String amount) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType("payment_notice");
        notification.setTitle("支付成功");
        notification.setContent("订单 " + orderNo + " 已成功支付，金额：" + amount);
        notification.setIsRead(0);
        notification.setCreatedAt(new Date());
        notification.setUpdatedAt(new Date());
        this.save(notification);
    }

    @Override
    public void createDeliveryNotification(Long userId, String orderNo, String status) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType("delivery_notice");
        notification.setTitle("配送状态更新");
        notification.setContent("订单 " + orderNo + " 配送状态：" + status);
        notification.setIsRead(0);
        notification.setCreatedAt(new Date());
        notification.setUpdatedAt(new Date());
        this.save(notification);
    }
} 