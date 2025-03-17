package com.itmk.web.controller;

import com.itmk.annotation.RequireRole;
import com.itmk.constant.SystemConstants;
import com.itmk.dto.MerchantProfileDTO;
import com.itmk.entity.Merchant;
import com.itmk.result.ResultVo;
import com.itmk.service.MerchantService;
import com.itmk.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "商家管理")
@RestController
@RequestMapping("/api/merchant")
@RequireRole(SystemConstants.ROLE_MERCHANT)
public class MerchantController {

    @Autowired
    private MerchantService merchantService;


    @ApiOperation("获取商家信息")
    @GetMapping("/profile")
    public ResultVo<MerchantProfileDTO> getProfile() {
        Long userId = UserContext.getCurrentUserId();
        Merchant merchant = merchantService.getByUserId(userId);
        MerchantProfileDTO profile = new MerchantProfileDTO();
        BeanUtils.copyProperties(merchant, profile);
        return ResultVo.success(profile);
    }

    @ApiOperation("更新商家信息")
    @PutMapping("/profile")
    public ResultVo<?> updateProfile(@RequestBody MerchantProfileDTO profileDTO) {
        Long userId = UserContext.getCurrentUserId();
        Merchant merchant = merchantService.getByUserId(userId);
        BeanUtils.copyProperties(profileDTO, merchant);
        return ResultVo.success(merchantService.updateById(merchant));
    }

} 