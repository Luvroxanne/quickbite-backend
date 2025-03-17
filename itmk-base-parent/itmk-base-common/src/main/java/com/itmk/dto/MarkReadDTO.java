package com.itmk.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("标记已读请求参数")
public class MarkReadDTO {
    @ApiModelProperty("通知ID，为空则标记所有通知为已读")
    private Long id;
}