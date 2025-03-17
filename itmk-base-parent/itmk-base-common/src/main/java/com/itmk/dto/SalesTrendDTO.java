package com.itmk.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("销售趋势数据")
public class SalesTrendDTO {
    @ApiModelProperty("日期（格式：MM-dd 或 yyyy-MM）")
    private String date;
    
    @ApiModelProperty("销售金额")
    private BigDecimal amount;
}
