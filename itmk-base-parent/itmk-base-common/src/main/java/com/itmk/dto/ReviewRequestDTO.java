package com.itmk.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.*;
import java.util.List;

@Data
@ApiModel("评论请求参数")
public class ReviewRequestDTO {
    @NotNull(message = "订单ID不能为空")
    @ApiModelProperty("订单ID")
    private Long orderId;

    @NotNull(message = "商家ID不能为空")
    @ApiModelProperty("商家ID")
    private Long merchantId;

    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最小为1")
    @Max(value = 5, message = "评分最大为5")
    @ApiModelProperty("评分(1-5)")
    private Integer rating;

    @NotBlank(message = "评论内容不能为空")
    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("图片数组")
    private List<String> images;
}