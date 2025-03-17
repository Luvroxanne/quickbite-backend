package com.itmk.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * @Author: MK
 * @Description: 订单请求DTO
 * @Date: 2024/3/20
 */
@Data
@ApiModel("订单请求参数")
public class OrderRequestDTO {
    
    @NotNull(message = "商家ID不能为空")
    @ApiModelProperty("商家ID")
    private Long merchantId;
    
    @NotBlank(message = "支付方式不能为空")
    @ApiModelProperty("支付方式")
    private String paymentMethod;
    
    @ApiModelProperty("备注")
    private String remark;
    
    @NotBlank(message = "配送地址不能为空")
    @Size(max = 255, message = "配送地址长度不能超过255")
    @ApiModelProperty("配送地址")
    private String address;
    
    @NotBlank(message = "联系人不能为空")
    @Size(max = 50, message = "联系人长度不能超过50")
    @ApiModelProperty("联系人")
    private String contactName;
    
    @NotBlank(message = "联系电话不能为空")
    @Size(max = 20, message = "联系电话长度不能超过20")
    @ApiModelProperty("联系电话")
    private String contactPhone;
    
    @NotEmpty(message = "订单项不能为空")
    @ApiModelProperty("订单商品列表")
    private List<OrderItemDTO> items;
    
    @Data
    public static class OrderItemDTO {
        @NotNull(message = "菜品ID不能为空")
        @ApiModelProperty("菜品ID")
        private Long dishId;
        
        @Min(value = 1, message = "数量必须大于0")
        @ApiModelProperty("数量")
        private Integer quantity;
    }
} 