package com.itmk.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itmk.dto.OrderRequestDTO;
import com.itmk.entity.Order;
import com.itmk.dto.OrderStatsDTO;
import com.itmk.dto.OrderRatingDTO;
import com.itmk.dto.OrderDetailDTO;
import java.util.List;

/**
 * @Author: MK
 * @Description: 订单服务接口
 * @Date: 2024/3/20
 */
public interface OrderService extends IService<Order> {
    /**
     * 获取商家订单列表
     */
    List<Order> getMerchantOrders(Long merchantId);

    /**
     * 创建订单
     * @param orderRequest 订单请求参数
     * @param userId 用户ID
     * @return 订单ID
     */
    Long createOrder(OrderRequestDTO orderRequest, Long userId);

    /**
     * 商家更新订单状态（准备中->配送中）
     */
    boolean merchantUpdateStatus(Long id);

    /**
     * 骑手更新订单状态（配送中->已完成）
     */
    boolean riderUpdateStatus(Long id);

    /**
     * 分页查询订单列表
     * @param page 页码
     * @param pageSize 每页数量
     * @param status 订单状态
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param userId 用户ID
     * @return 分页结果
     */
    Page<Order> listOrders(Integer page, Integer pageSize, String status, 
                          String startDate, String endDate, Long userId);

    /**
     * 获取订单统计信息
     * @param merchantId 商家ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计信息
     */
    OrderStatsDTO getOrderStats(Long merchantId, String startDate, String endDate);

    /**
     * 评价订单
     * @param id 订单ID
     * @param ratingDTO 评价信息
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean rateOrder(Long id, OrderRatingDTO ratingDTO, Long userId);

    /**
     * 申请退款
     * @param id 订单ID
     * @param reason 退款原因
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean refundOrder(Long id, String reason, Long userId);

    /**
     * 获取详细订单统计信息
     * @param merchantId 商家ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 详细统计信息
     */
    List<OrderDetailDTO> getDetailedOrderStats(Long merchantId, String startDate, String endDate);
} 