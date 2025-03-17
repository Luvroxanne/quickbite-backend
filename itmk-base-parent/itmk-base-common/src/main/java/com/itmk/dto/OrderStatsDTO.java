package com.itmk.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: MK
 * @Description: 订单统计DTO
 * @Date: 2024/3/20
 */
@Data
@ApiModel("订单统计信息")
public class OrderStatsDTO {
    @ApiModelProperty("总订单数")
    private Integer totalOrders;
    
    @ApiModelProperty("总金额")
    private BigDecimal totalAmount;
    
    @ApiModelProperty("待支付订单数")
    private Integer pendingOrders;
    
    @ApiModelProperty("已完成订单数")
    private Integer completedOrders;
    
    @ApiModelProperty("已取消订单数")
    private Integer cancelledOrders;
} 