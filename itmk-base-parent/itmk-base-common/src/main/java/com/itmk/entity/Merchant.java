package com.itmk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @Author: MK
 * @Description: 商家实体类
 * @Date: 2024/3/20
 */
@Data
@TableName("merchants")
@ApiModel("商家信息")
public class Merchant {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    
    /**
     * 店铺名称
     */
    @TableField("store_name")
    private String storeName;
    
    /**
     * 店铺地址
     */
    private String address;
    
    /**
     * 店铺logo
     */
    private String logo;
    
    /**
     * 店铺描述
     */
    private String description;
    
    /**
     * 营业时间
     */
    @TableField("business_hours")
    private String businessHours;
    
    /**
     * 店铺评分
     */
    @ApiModelProperty("商家评分")
    private Double rating;
    
    /**
     * 状态 1:营业中 0:休息中
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    @TableField("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;
    
    /**
     * 更新时间
     */
    @TableField("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedAt;
} 