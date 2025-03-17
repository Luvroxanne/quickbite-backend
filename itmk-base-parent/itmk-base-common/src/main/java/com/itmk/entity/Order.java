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

/**
 * @Author: MK
 * @Description: 订单实体类
 * @Date: 2024/3/20
 */
@Data
@TableName("orders")
@ApiModel("订单信息")
public class Order {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键ID")
    private Long id;

    /**
     * 订单编号
     */
    @TableField("order_no")
    @ApiModelProperty("订单编号")
    private String orderNo;

    /**
     * 商家ID
     */
    @TableField("merchant_id")
    @ApiModelProperty("商家ID")
    private Long merchantId;

    /**
     * 客户ID
     */
    @TableField("customer_id")
    @ApiModelProperty("客户ID")
    private Long customerId;

    /**
     * 订单总金额
     */
    @TableField("total_amount")
    @ApiModelProperty("订单总金额")
    private BigDecimal totalAmount;

    /**
     * 订单状态
     */
    @ApiModelProperty("订单状态：pending-待处理，paid-已支付，preparing-准备中，delivering-配送中，completed-已完成")
    private String status;

    /**
     * 支付方式
     */
    @TableField("payment_method")
    @ApiModelProperty("支付方式：WECHAT-微信支付，ALIPAY-支付宝")
    private String paymentMethod;

    /**
     * 支付状态
     */
    @TableField("payment_status")
    @ApiModelProperty("支付状态：0-未支付，1-已支付")
    private Integer paymentStatus;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 配送地址
     */
    @ApiModelProperty("配送地址")
    private String address;

    /**
     * 联系人
     */
    @TableField("contact_name")
    @ApiModelProperty("联系人")
    private String contactName;

    /**
     * 联系电话
     */
    @TableField("contact_phone")
    @ApiModelProperty("联系电话")
    private String contactPhone;

    /**
     * 创建时间
     */
    @TableField("created_at")
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;

    /**
     * 更新时间
     */
    @TableField("updated_at")
    @ApiModelProperty("更新时间")
    private Date updatedAt;

    @ApiModelProperty("退款状态(0:未退款,1:已退款)")
    private Integer refundStatus;
    
    @ApiModelProperty("退款时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date refundTime;
    
    @ApiModelProperty("退款原因")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String refundReason;
}