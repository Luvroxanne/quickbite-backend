package com.itmk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("sales_statistics")
@ApiModel("销售统计信息")
public class SalesStatistics {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("统计ID")
    private Long id;

    @TableField("merchant_id")
    @ApiModelProperty("商家ID")
    private Long merchantId;

    @ApiModelProperty("统计日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date date;

    @TableField("total_amount")
    @ApiModelProperty("总金额")
    private BigDecimal totalAmount;

    @TableField("order_count")
    @ApiModelProperty("订单数")
    private Integer orderCount;

    @TableField("customer_count")
    @ApiModelProperty("顾客数")
    private Integer customerCount;

    @TableField("created_at")
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;
}