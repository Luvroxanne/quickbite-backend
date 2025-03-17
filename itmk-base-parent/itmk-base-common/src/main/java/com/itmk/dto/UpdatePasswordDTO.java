package com.itmk.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@ApiModel("修改密码请求")
public class UpdatePasswordDTO {
    
    @NotEmpty(message = "原密码不能为空")
    @ApiModelProperty("原密码")
    private String oldPassword;
    
    @NotEmpty(message = "新密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20位之间")
    @ApiModelProperty("新密码")
    private String newPassword;
} 