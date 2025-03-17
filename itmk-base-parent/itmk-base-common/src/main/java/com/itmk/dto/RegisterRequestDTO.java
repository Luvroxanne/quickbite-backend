package com.itmk.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: MK
 * @Description: 注册请求DTO
 * @Date: 2024/3/20
 */
@Data
@ApiModel("注册请求参数")
public class RegisterRequestDTO {
    /**
     * 用户类型
     */
    @ApiModelProperty("用户类型:merchant/admin/customer")
    private String userType;
    
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
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;
    
    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;
    
    /**
     * 店铺名称(商家必填)
     */
    @ApiModelProperty("店铺名称(商家必填)")
    private String storeName;
    
    /**
     * 店铺地址(商家必填)
     */
    @ApiModelProperty("店铺地址(商家必填)")
    private String address;

    @ApiModelProperty("来源(web/app)")
    
    private String source;
} 