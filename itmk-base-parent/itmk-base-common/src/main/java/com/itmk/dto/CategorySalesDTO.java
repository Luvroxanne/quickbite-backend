package com.itmk.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("菜品分类销售量统计")
public class CategorySalesDTO {
    @ApiModelProperty("菜品分类名称")
    private String category;
    
    @ApiModelProperty("销售数量")
    private Integer count;
}
