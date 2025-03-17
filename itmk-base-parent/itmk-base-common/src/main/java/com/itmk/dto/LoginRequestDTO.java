package com.itmk.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: MK
 * @Description: 登录请求DTO
 * @Date: 2024/3/20
 */
@Data
@ApiModel("登录请求参数")
public class LoginRequestDTO {
    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;
    
    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;
    
    /**
     * 用户角色
     */
    @ApiModelProperty("用户角色，可选值：admin, merchant, customer, rider")
    private String userType;
}