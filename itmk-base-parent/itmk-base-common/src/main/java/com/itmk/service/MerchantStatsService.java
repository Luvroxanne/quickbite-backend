package com.itmk.service;

import com.itmk.dto.CategorySalesDTO;
import com.itmk.dto.MerchantStatsDTO;
import com.itmk.dto.SalesTrendDTO;

import java.util.List;

public interface MerchantStatsService {
    /**
     * 获取商家统计信息
     */
    MerchantStatsDTO getMerchantStats(Long merchantId);
    
    /**
     * 获取销售趋势数据
     * @param merchantId 商家ID
     * @param type 统计类型：week-本周, month-本月
     * @return 销售趋势数据列表
     */
    List<SalesTrendDTO> getSalesTrend(Long merchantId, String type);
    
    /**
     * 获取菜品分类销售量统计
     * @param merchantId 商家ID
     * @return 菜品分类销售量统计列表
     */
    List<CategorySalesDTO> getCategorySales(Long merchantId);
}