package com.itmk.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

/**
 * @Author: MK
 * @Description: 更新用户信息DTO
 * @Date: 2024/3/20
 */
@Data
@ApiModel("更新用户信息请求")
public class UpdateUserInfoDTO {
    
    @ApiModelProperty("手机号")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    @ApiModelProperty("邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;
    
    @ApiModelProperty("头像")
    private String avatar;
    @ApiModelProperty("密码")
    private String password;
} 