package com.itmk.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("商家统计信息")
public class MerchantStatsDTO {
    @ApiModelProperty("今日订单数")
    private Integer todayOrders;
    
    @ApiModelProperty("今日订单数增长率（相比昨日，百分比）")
    private Double todayOrdersGrowth;
    
    @ApiModelProperty("今日营收")
    private BigDecimal todayRevenue;
    
    @ApiModelProperty("今日营收增长率（相比昨日，百分比）")
    private Double todayRevenueGrowth;
    
    @ApiModelProperty("昨日订单数")
    private Integer yesterdayOrders;
    
    @ApiModelProperty("昨日收入")
    private BigDecimal yesterdayRevenue;
    
    @ApiModelProperty("总顾客数")
    private Integer totalCustomers;
    
    @ApiModelProperty("待处理订单数（准备中状态）")
    private Integer pendingOrders;
    
    @ApiModelProperty("平均评分（保留一位小数）")
    private Double avgRating;
    
    @ApiModelProperty("总销售额")
    private BigDecimal totalSales;
    
    @ApiModelProperty("销售趋势数据（本周或本月）")
    private List<SalesTrendDTO> salesTrend;
    
    @ApiModelProperty("菜品分类销售量统计")
    private List<CategorySalesDTO> categorySales;
}