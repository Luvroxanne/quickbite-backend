package com.itmk.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itmk.entity.OrderItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("订单详细信息")
public class OrderDetailDTO {
    @ApiModelProperty("订单ID")
    private Long id;
    
    @ApiModelProperty("订单编号")
    private String orderNo;
    
    @ApiModelProperty("商家ID")
    private Long merchantId;
    
    @ApiModelProperty("顾客ID")
    private Long customerId;
    
    @ApiModelProperty("订单总金额")
    private BigDecimal totalAmount;
    
    @ApiModelProperty("订单状态")
    private String status;
    
    @ApiModelProperty("支付方式")
    private String paymentMethod;
    
    @ApiModelProperty("支付状态")
    private Integer paymentStatus;
    
    @ApiModelProperty("备注")
    private String remark;
    
    @ApiModelProperty("配送地址")
    private String address;
    
    @ApiModelProperty("联系人")
    private String contactName;
    
    @ApiModelProperty("联系电话")
    private String contactPhone;
    
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;
    
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedAt;
    
    @ApiModelProperty("订单项列表")
    private List<OrderItem> orderItems;
} 