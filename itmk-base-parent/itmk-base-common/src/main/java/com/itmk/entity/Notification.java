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
@TableName("notifications")
@ApiModel("通知信息")
public class Notification {
    
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("通知ID")
    private Long id;
    
    @TableField("user_id")
    @ApiModelProperty("用户ID")
    private Long userId;
    
    @ApiModelProperty("通知标题")
    private String title;
    
    @ApiModelProperty("通知内容")
    private String content;
    
    @ApiModelProperty("通知类型")
    private String type;
    
    @TableField("is_read")
    @ApiModelProperty("是否已读(0:未读 1:已读)")
    private Integer isRead;

    @TableField("created_at")
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;

    @TableField("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedAt;
}