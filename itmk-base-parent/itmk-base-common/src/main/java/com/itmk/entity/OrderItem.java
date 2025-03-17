package com.itmk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: MK
 * @Description: 订单详情实体类
 * @Date: 2024/3/20
 */
@Data
@TableName("order_items")
@ApiModel("订单详情信息")
public class OrderItem {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键ID")
    private Long id;

    /**
     * 订单ID
     */
    @ApiModelProperty("订单ID")
    private Long orderId;

    /**
     * 菜品ID
     */
    @ApiModelProperty("菜品ID")
    private Long dishId;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private Integer quantity;

    /**
     * 单价
     */
    @ApiModelProperty("单价")
    private BigDecimal price;

    /**
     * 小计
     */
    @ApiModelProperty("小计")
    private BigDecimal subtotal;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;
} 