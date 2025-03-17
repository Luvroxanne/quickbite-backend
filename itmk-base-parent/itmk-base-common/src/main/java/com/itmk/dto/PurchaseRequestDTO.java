package com.itmk.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("采购申请参数")
public class PurchaseRequestDTO {
    @NotNull(message = "物品ID不能为空")
    @ApiModelProperty("物品ID")
    private Long itemId;
    
    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量必须大于0")
    @ApiModelProperty("采购数量")
    private Integer quantity;
    
    @NotNull(message = "紧急程度不能为空")
    @ApiModelProperty("紧急程度(normal:普通,urgent:紧急,very-urgent:非常紧急)")
    private String urgency;
} 