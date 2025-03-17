package com.itmk.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: MK
 * @Description: 用户信息DTO
 * @Date: 2024/3/20
 */
@Data
@ApiModel("用户信息")
public class UserInfoDTO {
    @ApiModelProperty("用户名")
    private String username;
    
    @ApiModelProperty("手机号")
    private String phone;
    
    @ApiModelProperty("邮箱")
    private String email;
    
    @ApiModelProperty("头像")
    private String avatar;
    
    @ApiModelProperty("角色")
    private String role;
} 