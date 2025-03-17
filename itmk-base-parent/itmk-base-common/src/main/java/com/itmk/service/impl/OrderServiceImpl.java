package com.itmk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itmk.dto.OrderRequestDTO;
import com.itmk.entity.Dish;
import com.itmk.entity.Order;
import com.itmk.entity.OrderItem;
import com.itmk.exception.BusinessException;
import com.itmk.mapper.OrderMapper;
import com.itmk.service.DishService;
import com.itmk.service.OrderItemService;
import com.itmk.service.OrderService;
import com.itmk.dto.OrderStatsDTO;
import com.itmk.dto.OrderRatingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import com.itmk.constant.OrderStatus;
import com.itmk.service.MerchantService;
import com.itmk.entity.Merchant;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import com.itmk.dto.OrderRequestDTO.OrderItemDTO;
import com.itmk.dto.OrderDetailDTO;
import com.itmk.service.ReviewService;
import com.itmk.entity.Review;
import com.itmk.dto.ReviewRequestDTO;

/**
 * @Author: MK
 * @Description: 订单服务实现类
 * @Date: 2024/3/20
 */
@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private DishService dishService;
    
    @Autowired
    private OrderItemService orderItemService;
    
    @Autowired
    private MerchantService merchantService;

    @Autowired
    private ReviewService reviewService;

    @Override
    public List<Order> getMerchantOrders(Long merchantId) {
        // 构建查询条件
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getMerchantId, merchantId)
              .orderByDesc(Order::getCreatedAt);
        
        return this.list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOrder(OrderRequestDTO orderRequest, Long userId) {
        // 1. 创建订单
        Order order = new Order();
        order.setOrderNo(UUID.randomUUID().toString().replace("-", ""));
        order.setMerchantId(orderRequest.getMerchantId());
        order.setCustomerId(userId);
        order.setStatus(OrderStatus.PENDING.getCode()); // 使用枚举
        order.setPaymentMethod(orderRequest.getPaymentMethod());
        order.setPaymentStatus(0);
        order.setRemark(orderRequest.getRemark());
        order.setAddress(orderRequest.getAddress());
        order.setContactName(orderRequest.getContactName());
        order.setContactPhone(orderRequest.getContactPhone());
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());

        // 2. 计算订单总金额并创建订单项
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
        
        for (OrderItemDTO item : orderRequest.getItems()) {
            Dish dish = dishService.getById(item.getDishId());
            if (dish == null) {
                throw new BusinessException("菜品不存在");
            }
            
            // 计算小计金额
            BigDecimal subtotal = dish.getPrice().multiply(new BigDecimal(item.getQuantity()));
            totalAmount = totalAmount.add(subtotal);

            // 创建订单项
            OrderItem orderItem = new OrderItem();
            orderItem.setDishId(item.getDishId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(dish.getPrice());
            orderItem.setSubtotal(subtotal);
            orderItem.setCreatedAt(new Date());
            orderItems.add(orderItem);
        }

        // 3. 设置订单总金额
        order.setTotalAmount(totalAmount);
        this.save(order);

        // 4. 保存订单项
        orderItems.forEach(item -> item.setOrderId(order.getId()));
        orderItemService.saveBatch(orderItems);

        return order.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean merchantUpdateStatus(Long id) {
        Order order = this.getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 验证订单状态
        if (!OrderStatus.PREPARING.getCode().equals(order.getStatus())) {
            throw new BusinessException("只能更新准备中的订单状态");
        }
        
        // 更新状态为配送中
        order.setStatus(OrderStatus.DELIVERING.getCode());
        order.setUpdatedAt(new Date());
        
        return this.updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean riderUpdateStatus(Long id) {
        Order order = this.getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 验证订单状态
        if (!OrderStatus.DELIVERING.getCode().equals(order.getStatus())) {
            throw new BusinessException("只能更新配送中的订单状态");
        }
        
        // 更新状态为已完成
        order.setStatus(OrderStatus.COMPLETED.getCode());
        order.setUpdatedAt(new Date());
        
        return this.updateById(order);
    }

    /**
     * 检查状态流转是否合法
     */
    private void checkStatusTransition(String currentStatus, String targetStatus) {
        // 定义状态流转规则
        Map<String, List<String>> statusFlow = new HashMap<>();
        statusFlow.put(OrderStatus.PENDING.getCode(), Arrays.asList(OrderStatus.PAID.getCode(), OrderStatus.CANCELLED.getCode()));
        statusFlow.put(OrderStatus.PAID.getCode(), Collections.singletonList(OrderStatus.PREPARING.getCode()));
        statusFlow.put(OrderStatus.PREPARING.getCode(), Collections.singletonList(OrderStatus.DELIVERING.getCode()));
        statusFlow.put(OrderStatus.DELIVERING.getCode(), Collections.singletonList(OrderStatus.COMPLETED.getCode()));
        
        List<String> allowedStatus = statusFlow.get(currentStatus);
        if (allowedStatus == null || !allowedStatus.contains(targetStatus)) {
            throw new BusinessException("订单状态流转不合法");
        }
    }

    /**
     * 更新菜品销量
     */
    private void updateDishSales(Long orderId) {
        // 1. 获取订单项
        List<OrderItem> orderItems = orderItemService.lambdaQuery()
                .eq(OrderItem::getOrderId, orderId)
                .list();
        
        // 2. 更新菜品销量
        for (OrderItem item : orderItems) {
            dishService.lambdaUpdate()
                    .setSql("sales = sales + " + item.getQuantity())
                    .eq(Dish::getId, item.getDishId())
                    .update();
        }
    }

    @Override
    public Page<Order> listOrders(Integer page, Integer pageSize, String status, 
                                String startDate, String endDate, Long userId) {
        Page<Order> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        
        // 添加查询条件
        wrapper.eq(Order::getCustomerId, userId);
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        
        // 添加日期范围查询
        if (startDate != null) {
            wrapper.ge(Order::getCreatedAt, startDate + " 00:00:00");
        }
        if (endDate != null) {
            wrapper.le(Order::getCreatedAt, endDate + " 23:59:59");
        }
        
        // 按创建时间降序排序
        wrapper.orderByDesc(Order::getCreatedAt);
        
        return this.page(pageParam, wrapper);
    }

    @Override
    public OrderStatsDTO getOrderStats(Long merchantId, String startDate, String endDate) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getMerchantId, merchantId);
        
        // 添加日期范围
        if (startDate != null) {
            wrapper.ge(Order::getCreatedAt, startDate + " 00:00:00");
        }
        if (endDate != null) {
            wrapper.le(Order::getCreatedAt, endDate + " 23:59:59");
        }
        
        // 查询订单列表
        List<Order> orders = this.list(wrapper);
        
        // 统计数据
        OrderStatsDTO stats = new OrderStatsDTO();
        stats.setTotalOrders(orders.size());
        stats.setTotalAmount(orders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        stats.setPendingOrders((int) orders.stream()
                .filter(o -> OrderStatus.PENDING.getCode().equals(o.getStatus()))
                .count());
        stats.setCompletedOrders((int) orders.stream()
                .filter(o -> OrderStatus.COMPLETED.getCode().equals(o.getStatus()))
                .count());
        stats.setCancelledOrders((int) orders.stream()
                .filter(o -> OrderStatus.CANCELLED.getCode().equals(o.getStatus()))
                .count());
        
        return stats;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rateOrder(Long id, OrderRatingDTO ratingDTO, Long userId) {
        try {
            // 1. 检查订单是否存在
            Order order = this.getById(id);
            if (order == null) {
                throw new BusinessException("订单不存在");
            }
            
            // 2. 检查订单是否属于当前用户
            if (!order.getCustomerId().equals(userId)) {
                throw new BusinessException("无权评价此订单");
            }
            
            // 3. 检查订单状态是否为已完成
            if (!OrderStatus.COMPLETED.getCode().equals(order.getStatus())) {
                throw new BusinessException("只能评价已完成的订单");
            }
            
            // 4. 检查订单是否已评价
            if (reviewService.existsByOrderId(id)) {
                throw new BusinessException("订单已评价");
            }
            
            // 5. 创建评价请求DTO
            ReviewRequestDTO reviewRequest = new ReviewRequestDTO();
            reviewRequest.setOrderId(id);
            reviewRequest.setMerchantId(order.getMerchantId());
            reviewRequest.setRating(ratingDTO.getRating());
            reviewRequest.setContent(ratingDTO.getComment());
            reviewRequest.setImages(new ArrayList<>()); // 如果需要图片，可以从ratingDTO中获取
            
            // 6. 添加评价
            return reviewService.addReview(reviewRequest, userId);
        } catch (Exception e) {
            log.error("评价订单失败", e);
            throw new BusinessException("评价订单失败: " + e.getMessage());
        }
    }

    @Override
    public boolean refundOrder(Long id, String reason, Long userId) {
        // 1. 检查订单是否存在
        Order order = this.getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 2. 检查订单是否属于当前用户
        if (!order.getCustomerId().equals(userId)) {
            throw new BusinessException("无权申请退款");
        }
        
        // 3. 检查订单状态是否为已支付
        if (!OrderStatus.PAID.getCode().equals(order.getStatus())) {
            throw new BusinessException("只能对已支付的订单申请退款");
        }
        
        // 4. 检查是否已退款
        if (order.getRefundStatus() != null && order.getRefundStatus() == 1) {
            throw new BusinessException("订单已退款");
        }
        
        // 5. 更新订单退款信息
        order.setRefundStatus(1);
        order.setRefundTime(new Date());
        order.setRefundReason(reason);
        order.setStatus(OrderStatus.CANCELLED.getCode());
        
        return this.updateById(order);
    }

    /**
     * 更新商家评分
     */
    private void updateMerchantRating(Long merchantId) {
        // 1. 查询商家所有已评价订单的评价
        List<Review> reviews = reviewService.lambdaQuery()
                .eq(Review::getMerchantId, merchantId)
                .list();
        
        // 2. 计算平均评分
        double avgRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(5.0);
        
        // 3. 更新商家评分
        merchantService.lambdaUpdate()
                .set(Merchant::getRating, avgRating)
                .eq(Merchant::getId, merchantId)
                .update();
    }

    @Override
    public List<OrderDetailDTO> getDetailedOrderStats(Long merchantId, String startDate, String endDate) {
        try {
            // 1. 查询订单列表
            LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
            orderWrapper.eq(Order::getMerchantId, merchantId)
                    .ge(startDate != null, Order::getCreatedAt, startDate + " 00:00:00")
                    .le(endDate != null, Order::getCreatedAt, endDate + " 23:59:59")
                    .orderByDesc(Order::getCreatedAt);
            
            List<Order> orders = this.list(orderWrapper);
            
            // 2. 查询订单项并组装数据
            List<OrderDetailDTO> result = new ArrayList<>();
            for (Order order : orders) {
                OrderDetailDTO detailDTO = new OrderDetailDTO();
                // 复制订单基本信息
                detailDTO.setId(order.getId());
                detailDTO.setOrderNo(order.getOrderNo());
                detailDTO.setMerchantId(order.getMerchantId());
                detailDTO.setCustomerId(order.getCustomerId());
                detailDTO.setTotalAmount(order.getTotalAmount());
                detailDTO.setStatus(order.getStatus());
                detailDTO.setPaymentMethod(order.getPaymentMethod());
                detailDTO.setPaymentStatus(order.getPaymentStatus());
                detailDTO.setRemark(order.getRemark());
                detailDTO.setAddress(order.getAddress());
                detailDTO.setContactName(order.getContactName());
                detailDTO.setContactPhone(order.getContactPhone());
                detailDTO.setCreatedAt(order.getCreatedAt());
                detailDTO.setUpdatedAt(order.getUpdatedAt());
                
                // 查询订单项
                List<OrderItem> items = orderItemService.lambdaQuery()
                        .eq(OrderItem::getOrderId, order.getId())
                        .list();
                detailDTO.setOrderItems(items);
                
                result.add(detailDTO);
            }
            
            return result;
        } catch (Exception e) {
            log.error("获取订单详情失败", e);
            throw new BusinessException("获取订单详情失败: " + e.getMessage());
        }
    }
} 