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
@TableName("deliveries")
@ApiModel("配送信息")
public class Delivery {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("配送ID")
    private Long id;

    @TableField("order_id")
    @ApiModelProperty("订单ID")
    private Long orderId;

    @TableField("rider_id")
    @ApiModelProperty("骑手ID")
    private Long riderId;

    @TableField("merchant_id")
    @ApiModelProperty("商家ID")
    private Long merchantId;

    @ApiModelProperty("配送状态")
    private String status;

    @TableField("estimated_time")
    @ApiModelProperty("预计送达时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String estimatedTime;

    @TableField("actual_time")
    @ApiModelProperty("实际送达时间")
    private Date actualTime;

    @ApiModelProperty("配送距离")
    private BigDecimal distance;

    @ApiModelProperty("配送费")
    private BigDecimal fee;

    @TableField("created_at")
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;

    @TableField("updated_at")
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedAt;

    // 关联订单信息（非数据库字段）
    @TableField(exist = false)
    @ApiModelProperty("订单信息")
    private Order order;
}