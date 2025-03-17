package com.itmk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName("reviews")
@ApiModel("评价信息")
public class Review {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("评价ID")
    private Long id;

    @TableField("order_id")
    @ApiModelProperty("订单ID")
    private Long orderId;

    @TableField("customer_id")
    @ApiModelProperty("顾客ID")
    private Long customerId;

    @TableField("merchant_id")
    @ApiModelProperty("商家ID")
    private Long merchantId;

    @ApiModelProperty("评分")
    private Integer rating;

    @ApiModelProperty("评价内容")
    private String content;

    @ApiModelProperty("图片JSON数组")
    private String images;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("商家回复")
    private String reply;

    @ApiModelProperty("回复时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date replyTime;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedAt;
}