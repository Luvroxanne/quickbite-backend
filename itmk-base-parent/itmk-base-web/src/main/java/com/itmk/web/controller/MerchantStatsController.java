package com.itmk.web.controller;

import com.itmk.annotation.RequireRole;
import com.itmk.constant.SystemConstants;
import com.itmk.dto.CategorySalesDTO;
import com.itmk.dto.MerchantStatsDTO;
import com.itmk.dto.SalesTrendDTO;
import com.itmk.result.ResultVo;
import com.itmk.service.MerchantStatsService;
import com.itmk.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "商家统计")
@RestController
@RequestMapping("/api/merchant/stats")
@RequireRole(SystemConstants.ROLE_MERCHANT)
public class MerchantStatsController {

    @Autowired
    private MerchantStatsService merchantStatsService;

    @ApiOperation("获取商家统计信息")
    @GetMapping
    public ResultVo<MerchantStatsDTO> getStats() {
        Long merchantId = UserContext.getCurrentUserId();
        return ResultVo.success(merchantStatsService.getMerchantStats(merchantId));
    }
    
    @ApiOperation("获取销售趋势数据")
    @GetMapping("/sales-trend")
    public ResultVo<List<SalesTrendDTO>> getSalesTrend(
            @ApiParam(value = "统计类型：week-本周, month-本月", required = true)
            @RequestParam(defaultValue = "week") String type) {
        Long merchantId = UserContext.getCurrentUserId();
        return ResultVo.success(merchantStatsService.getSalesTrend(merchantId, type));
    }
    
    @ApiOperation("获取菜品分类销售量统计")
    @GetMapping("/category-sales")
    public ResultVo<List<CategorySalesDTO>> getCategorySales() {
        Long merchantId = UserContext.getCurrentUserId();
        return ResultVo.success(merchantStatsService.getCategorySales(merchantId));
    }
}