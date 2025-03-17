package com.itmk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: MK
 * @Description: 菜品实体类
 * @Date: 2024/3/20
 */
@Data
@TableName("dishes")
@ApiModel("菜品信息")
public class Dish {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键ID")
    private Long id;

    /**
     * 商家ID
     */
    @TableField("merchant_id")
    @ApiModelProperty("商家ID")
    private Long merchantId;

    /**
     * 菜品名称
     */
    @NotBlank(message = "菜品名称不能为空")
    @ApiModelProperty("菜品名称")
    private String name;

    /**
     * 分类
     */
    @NotBlank(message = "菜品分类不能为空")
    @ApiModelProperty("分类")
    private String category;

    /**
     * 价格
     */
    @NotNull(message = "菜品价格不能为空")
    @DecimalMin(value = "0.01", message = "价格必须大于0")
    @ApiModelProperty("价格")
    private BigDecimal price;

    /**
     * 图片
     */
    private String image;

    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String description;

    /**
     * 状态 1:上架 0:下架
     */
    @ApiModelProperty("状态 1:上架 0:下架")
    private Boolean status;

    /**
     * 库存
     */
    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能小于0")
    @ApiModelProperty("库存")
    private Integer stock;

    /**
     * 销量
     */
    @ApiModelProperty("销量")
    private Integer sales;

    /**
     * 创建时间
     */
    @TableField("created_at")
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;

    /**
     * 更新时间
     */
    @TableField("updated_at")
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedAt;

    /**
     * 逻辑删除标志 1:已删除 0:未删除
     */
    @ApiModelProperty("逻辑删除标志 1:已删除 0:未删除")
    private Integer deleted;
}