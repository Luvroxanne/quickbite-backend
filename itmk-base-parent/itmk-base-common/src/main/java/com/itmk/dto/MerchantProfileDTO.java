package com.itmk.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("商家个人信息")
public class MerchantProfileDTO {
    
    @ApiModelProperty("商家ID")
    private Long id;
    
    @ApiModelProperty("店铺名称")
    private String storeName;
    
    @ApiModelProperty("店铺地址")
    private String address;
    
    @ApiModelProperty("店铺logo")
    private String logo;
    
    @ApiModelProperty("店铺描述")
    private String description;
    
    @ApiModelProperty("营业时间")
    private String businessHours;
    
    @ApiModelProperty("店铺评分")
    private Double rating;
    
    @ApiModelProperty("状态(1:营业中 0:休息中)")
    private Integer status;
} 