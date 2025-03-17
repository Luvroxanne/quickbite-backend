package com.itmk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("inventory")
@ApiModel("库存信息")
public class Inventory {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("库存ID")
    private Long id;

    @TableField("merchant_id")
    @ApiModelProperty("商家ID")
    private Long merchantId;

    @ApiModelProperty("原料名称")
    private String name;

    @ApiModelProperty("分类")
    private String category;

    @ApiModelProperty("库存量")
    private Integer stock;

    @ApiModelProperty("单位")
    private String unit;

    @TableField("warning_level")
    @ApiModelProperty("警戒库存")
    private Integer warningLevel;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("供应商")
    private String supplier;

    @ApiModelProperty("采购价")
    private BigDecimal price;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedAt;
}