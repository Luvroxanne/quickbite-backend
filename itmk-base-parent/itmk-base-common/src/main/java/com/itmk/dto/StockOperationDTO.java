package com.itmk.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("库存操作参数")
public class StockOperationDTO {
    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量必须大于0")
    @ApiModelProperty("操作数量")
    private Integer quantity;
    
    @NotBlank(message = "原因不能为空")
    @ApiModelProperty("操作原因")
    private String reason;
} 