package com.itmk.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Author: MK
 * @Description: 订单评价DTO
 * @Date: 2024/3/20
 */
@Data
@ApiModel("订单评价参数")
public class OrderRatingDTO {
    
    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最小为1")
    @Max(value = 5, message = "评分最大为5")
    @ApiModelProperty("评分(1-5)")
    private Integer rating;
    
    @ApiModelProperty("评价内容")
    private String comment;
} 