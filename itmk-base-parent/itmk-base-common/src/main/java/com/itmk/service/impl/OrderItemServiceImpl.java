package com.itmk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itmk.entity.OrderItem;
import com.itmk.mapper.OrderItemMapper;
import com.itmk.service.OrderItemService;
import org.springframework.stereotype.Service;

/**
 * @Author: MK
 * @Description: 订单项服务实现类
 * @Date: 2024/3/20
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {
} 