package com.itmk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itmk.entity.Delivery;
import com.itmk.entity.Order;
import com.itmk.entity.User;
import com.itmk.exception.BusinessException;
import com.itmk.mapper.DeliveryMapper;
import com.itmk.service.DeliveryService;
import com.itmk.service.OrderService;
import com.itmk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DeliveryServiceImpl extends ServiceImpl<DeliveryMapper, Delivery> implements DeliveryService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Override
    public List<Delivery> getDeliveryList(Long merchantId) {
        // 获取配送列表
        LambdaQueryWrapper<Delivery> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Delivery::getMerchantId, merchantId);
        List<Delivery> deliveries = this.list(wrapper);

        // 填充订单信息
        for (Delivery delivery : deliveries) {
            Order order = orderService.getById(delivery.getOrderId());
            delivery.setOrder(order);
        }

        // 按状态排序：待分配、已分配、配送中、已完成
        deliveries.sort((a, b) -> {
            Map<String, Integer> statusOrder = new HashMap<>();
            statusOrder.put("pending", 1);    // 待分配
            statusOrder.put("assigned", 2);   // 已分配
            statusOrder.put("delivering", 3); // 配送中
            statusOrder.put("completed", 4);  // 已完成
            
            Integer orderA = statusOrder.getOrDefault(a.getStatus(), 999);
            Integer orderB = statusOrder.getOrDefault(b.getStatus(), 999);
            
            if (orderA.equals(orderB)) {
                // 如果状态相同，按创建时间倒序
                return b.getCreatedAt().compareTo(a.getCreatedAt());
            }
            return orderA.compareTo(orderB);
        });

        return deliveries;
    }

    @Override
    public Map<String, Object> getDeliveryStats(Long merchantId) {
        Map<String, Object> stats = new HashMap<>();
        Date todayStart = getTodayStart();
        
        // 获取今日配送订单数
        long todayOrders = this.lambdaQuery()
                .eq(Delivery::getMerchantId, merchantId)
                .ge(Delivery::getCreatedAt, todayStart)
                .count();
        
        // 获取已完成配送数
        long completedOrders = this.lambdaQuery()
                .eq(Delivery::getMerchantId, merchantId)
                .eq(Delivery::getStatus, "completed")
                .count();
        
        // 计算平均配送时间（分钟）
        List<Delivery> completedDeliveries = this.lambdaQuery()
                .eq(Delivery::getMerchantId, merchantId)
                .eq(Delivery::getStatus, "completed")
                .isNotNull(Delivery::getActualTime)
                .list();
        
        double avgDeliveryTime = calculateAverageDeliveryTime(completedDeliveries);
        
        stats.put("todayOrders", todayOrders);
        stats.put("completedOrders", completedOrders);
        stats.put("avgDeliveryTime", avgDeliveryTime);
        
        return stats;
    }

    @Override
    public List<Map<String, Object>> getOnlineRiders(Long merchantId) {
        try {
            // 获取所有角色为rides的用户
            // 获取角色为rider的用户列表
            List<User> allRiders = userService.lambdaQuery()
                    .eq(User::getRole, "rider")
                    .list();
            if (allRiders.isEmpty()) {
                return new ArrayList<>();
            }

            // 获取当前正在配送的订单信息
            List<Delivery> activeDeliveries = this.lambdaQuery()
                    .eq(Delivery::getMerchantId, merchantId)
                    .eq(Delivery::getStatus, "delivering")
                    .list();

            // 创建骑手ID到配送订单的映射
            Map<Long, Delivery> riderDeliveryMap = activeDeliveries.stream()
                    .filter(d -> d.getRiderId() != null)
                    .collect(Collectors.toMap(
                            Delivery::getRiderId,
                            delivery -> delivery,
                            (existing, replacement) -> existing
                    ));

            // 组装所有骑手信息
            List<Map<String, Object>> riders = new ArrayList<>();
            for (User rider : allRiders) {
                Map<String, Object> riderInfo = new HashMap<>();
                riderInfo.put("riderId", rider.getId());
                riderInfo.put("username", rider.getUsername());
                riderInfo.put("phone", rider.getPhone());
                riderInfo.put("avatar", rider.getAvatar());
                riderInfo.put("email", rider.getEmail());

                // 检查骑手是否正在配送
                Delivery activeDelivery = riderDeliveryMap.get(rider.getId());
                if (activeDelivery != null) {
                    riderInfo.put("status", "delivering");
                    riderInfo.put("currentOrderId", activeDelivery.getOrderId());
                } else {
                    riderInfo.put("status", "idle");
                    riderInfo.put("currentOrderId", null);
                }

                // 获取骑手今日订单数
                long todayOrderCount = this.lambdaQuery()
                        .eq(Delivery::getRiderId, rider.getId())
                        .ge(Delivery::getCreatedAt, getTodayStart())
                        .count();
                riderInfo.put("todayOrderCount", todayOrderCount);

                riders.add(riderInfo);
            }

            return riders;
        } catch (Exception e) {
            log.error("获取骑手列表失败", e);
            throw new BusinessException("获取骑手列表失败: " + e.getMessage());
        }
    }

    // 获取今天开始时间
    private Date getTodayStart() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    // 计算平均配送时间（分钟）
    private double calculateAverageDeliveryTime(List<Delivery> deliveries) {
        if (deliveries.isEmpty()) {
            return 0.0;
        }

        long totalMinutes = 0;
        int count = 0;

        for (Delivery delivery : deliveries) {
            if (delivery.getActualTime() != null) {
                long diffInMillies = delivery.getActualTime().getTime() - delivery.getCreatedAt().getTime();
                totalMinutes += TimeUnit.MILLISECONDS.toMinutes(diffInMillies);
                count++;
            }
        }

        return count > 0 ? (double) totalMinutes / count : 0.0;
    }
}
