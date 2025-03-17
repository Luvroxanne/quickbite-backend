package com.itmk.web.controller;

import com.itmk.dto.OrderRequestDTO;
import com.itmk.dto.OrderRatingDTO;
import com.itmk.dto.OrderDetailDTO;
import com.itmk.entity.Order;
import com.itmk.result.ResultVo;
import com.itmk.service.OrderService;
import com.itmk.utils.UserContext;
import com.itmk.annotation.RequireRole;
import com.itmk.constant.SystemConstants;
import com.itmk.constant.OrderStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import java.util.List;

/**
 * @Author: MK
 * @Description: 订单控制器
 * @Date: 2024/3/20
 */
@Api(tags = "订单管理")
@RestController
@RequestMapping("/api/orders")
@Slf4j
@Validated
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequireRole(SystemConstants.ROLE_MERCHANT)
    @ApiOperation("获取完整的订单信息和订单详情（商家）")
    @GetMapping("/stats/detail")
    public ResultVo<?> getDetailedStats(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            // 获取当前登录的商家ID
            Long merchantId = UserContext.getCurrentUserId();
            
            // 获取订单详情
            List<OrderDetailDTO> details = orderService.getDetailedOrderStats(merchantId, startDate, endDate);
            
            return ResultVo.success(details);
        } catch (Exception e) {
            log.error("获取订单详情失败", e);
            return ResultVo.fail(e.getMessage());
        }
    }

    
    @ApiOperation("商家更新订单状态（准备中->配送中）")
    @PutMapping("/merchant/status/{id}")
    @RequireRole(SystemConstants.ROLE_MERCHANT)
    public ResultVo<?> merchantUpdateStatus(@PathVariable @NotNull(message = "订单ID不能为空") Long id) {
        try {
            // 验证当前订单状态是否为"准备中"
            Order order = orderService.getById(id);
            if (order == null) {
                return ResultVo.fail("订单不存在");
            }
            
            // 检查订单状态
            if (!OrderStatus.PREPARING.getCode().equals(order.getStatus())) {
                return ResultVo.fail("只能更新准备中的订单状态");
            }
            
            // 更新为"配送中"状态
            order.setStatus(OrderStatus.DELIVERING.getCode());
            boolean result = orderService.updateById(order);
            
            return result ? ResultVo.success("状态更新成功") : ResultVo.fail("状态更新失败");
        } catch (Exception e) {
            log.error("商家更新订单状态失败", e);
            return ResultVo.fail("更新订单状态失败: " + e.getMessage());
        }
    }

    @RequireRole(SystemConstants.ROLE_MERCHANT)
    @ApiOperation("返回订单的统计信息）")
    @GetMapping("/stats")
    public ResultVo<?> getStats(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        Long merchantId = UserContext.getCurrentUserId();
        return ResultVo.success(orderService.getOrderStats(merchantId, startDate, endDate));
    }

    @RequireRole(SystemConstants.ROLE_CUSTOMER)
    @ApiOperation("评价订单（顾客）")
    @PostMapping("/{id}/rate")
    public ResultVo<?> rate(
            @PathVariable @NotNull(message = "订单ID不能为空") Long id,
            @RequestBody @Valid OrderRatingDTO ratingDTO) {
        Long userId = UserContext.getCurrentUserId();
        return ResultVo.success(orderService.rateOrder(id, ratingDTO, userId));
    }

    @RequireRole(SystemConstants.ROLE_CUSTOMER)
    @ApiOperation("申请退款（顾客）")
    @PostMapping("/{id}/refund")
    public ResultVo<?> refund(
            @PathVariable @NotNull(message = "订单ID不能为空") Long id,
            @RequestParam @NotEmpty(message = "退款原因不能为空") String reason) {
        Long userId = UserContext.getCurrentUserId();
        return ResultVo.success(orderService.refundOrder(id, reason, userId));
    }

    @ApiOperation("商家获取订单列表")
    @GetMapping("/merchant/list")
    @RequireRole(SystemConstants.ROLE_MERCHANT)
    public ResultVo<?> getMerchantOrders() {
        Long merchantId = UserContext.getCurrentUserId();
        List<Order> orders = orderService.getMerchantOrders(merchantId);
        return ResultVo.success(orders);
    }

    @ApiOperation("顾客创建订单")
    @PostMapping("/create")
    @RequireRole(SystemConstants.ROLE_CUSTOMER)
    public ResultVo<?> createOrder(@RequestBody OrderRequestDTO orderRequest) {
        try {
            Long userId = UserContext.getCurrentUserId();
            Long orderId = orderService.createOrder(orderRequest, userId);
            return ResultVo.success(orderId);
        } catch (Exception e) {
            log.error("创建订单失败", e);
            return ResultVo.fail("创建订单失败: " + e.getMessage());
        }
    }

    @ApiOperation("骑手更新订单状态（配送中->已完成）")
    @PutMapping("/rider/status/{id}")
    @RequireRole(SystemConstants.ROLE_RIDER)
    public ResultVo<?> riderUpdateStatus(@PathVariable @NotNull(message = "订单ID不能为空") Long id) {
        try {
            // 验证当前订单状态是否为"配送中"
            Order order = orderService.getById(id);
            if (order == null) {
                return ResultVo.fail("订单不存在");
            }
            
            // 检查订单状态
            if (!OrderStatus.DELIVERING.getCode().equals(order.getStatus())) {
                return ResultVo.fail("只能更新配送中的订单状态");
            }
            
            // 更新为"已完成"状态
            order.setStatus(OrderStatus.COMPLETED.getCode());
            boolean result = orderService.updateById(order);
            
            return result ? ResultVo.success("状态更新成功") : ResultVo.fail("状态更新失败");
        } catch (Exception e) {
            log.error("骑手更新订单状态失败", e);
            return ResultVo.fail("更新订单状态失败: " + e.getMessage());
        }
    }

    @ApiOperation("顾客取消订单（待支付->已取消）")
    @PutMapping("/customer/cancel/{id}")
    @RequireRole(SystemConstants.ROLE_CUSTOMER)
    public ResultVo<?> customerCancelOrder(@PathVariable @NotNull(message = "订单ID不能为空") Long id) {
        try {
            // 验证当前订单状态是否为"待支付"
            Order order = orderService.getById(id);
            if (order == null) {
                return ResultVo.fail("订单不存在");
            }
            
            // 验证是否是当前用户的订单
            Long currentUserId = UserContext.getCurrentUserId();
            if (!order.getCustomerId().equals(currentUserId)) {
                return ResultVo.fail("无权操作此订单");
            }
            
            // 检查订单状态
            if (!OrderStatus.PENDING.getCode().equals(order.getStatus())) {
                return ResultVo.fail("只能取消待支付的订单");
            }
            
            // 更新为"已取消"状态
            order.setStatus(OrderStatus.CANCELLED.getCode());
            boolean result = orderService.updateById(order);
            
            return result ? ResultVo.success("订单取消成功") : ResultVo.fail("订单取消失败");
        } catch (Exception e) {
            log.error("顾客取消订单失败", e);
            return ResultVo.fail("取消订单失败: " + e.getMessage());
        }
    }

    @ApiOperation("支付订单（待支付->已支付）")
    @PutMapping("/customer/pay/{id}")
    @RequireRole(SystemConstants.ROLE_CUSTOMER)
    public ResultVo<?> customerPayOrder(@PathVariable @NotNull(message = "订单ID不能为空") Long id) {
        try {
            // 验证当前订单状态是否为"待支付"
            Order order = orderService.getById(id);
            if (order == null) {
                return ResultVo.fail("订单不存在");
            }
            
            // 验证是否是当前用户的订单
            Long currentUserId = UserContext.getCurrentUserId();
            if (!order.getCustomerId().equals(currentUserId)) {
                return ResultVo.fail("无权操作此订单");
            }
            
            // 检查订单状态
            if (!OrderStatus.PENDING.getCode().equals(order.getStatus())) {
                return ResultVo.fail("只能支付待支付状态的订单");
            }
            
            // 更新为"已支付"状态
            order.setStatus(OrderStatus.PAID.getCode());
            order.setPaymentStatus(1); // 设置支付状态为已支付
            boolean result = orderService.updateById(order);
            
            return result ? ResultVo.success("支付成功") : ResultVo.fail("支付失败");
        } catch (Exception e) {
            log.error("顾客支付订单失败", e);
            return ResultVo.fail("支付失败: " + e.getMessage());
        }
    }
} 