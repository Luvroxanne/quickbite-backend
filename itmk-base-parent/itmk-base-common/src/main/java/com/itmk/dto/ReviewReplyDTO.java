package com.itmk.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
@ApiModel("评论回复参数")
public class ReviewReplyDTO {
    @NotBlank(message = "回复内容不能为空")
    @ApiModelProperty("回复内容")
    private String reply;
}