package com.itmk.service.impl;

import com.itmk.dto.CategorySalesDTO;
import com.itmk.dto.MerchantStatsDTO;
import com.itmk.dto.SalesTrendDTO;
import com.itmk.mapper.MerchantStatsMapper;
import com.itmk.service.MerchantStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MerchantStatsServiceImpl implements MerchantStatsService {
    
    @Autowired
    private MerchantStatsMapper merchantStatsMapper;

    @Override
    public MerchantStatsDTO getMerchantStats(Long merchantId) {
        MerchantStatsDTO stats = new MerchantStatsDTO();
        
        // 获取今日统计
        Map<String, Object> todayStats = merchantStatsMapper.getTodayStats(merchantId);
        Long todayOrders = (Long) todayStats.get("todayOrders");
        BigDecimal todayRevenue = (BigDecimal) todayStats.get("todayRevenue");
        stats.setTodayOrders(todayOrders != null ? todayOrders.intValue() : 0);
        stats.setTodayRevenue(todayRevenue);
        
        // 获取昨日统计
        Long yesterdayOrders = merchantStatsMapper.getYesterdayOrders(merchantId);
        BigDecimal yesterdayRevenue = merchantStatsMapper.getYesterdayRevenue(merchantId);
        stats.setYesterdayOrders(yesterdayOrders != null ? yesterdayOrders.intValue() : 0);
        stats.setYesterdayRevenue(yesterdayRevenue);
        
        // 计算增长率
        if (yesterdayOrders != null && yesterdayOrders > 0) {
            double orderGrowth = ((double) todayOrders - yesterdayOrders) / yesterdayOrders * 100;
            stats.setTodayOrdersGrowth(Math.round(orderGrowth * 10) / 10.0);
        } else {
            stats.setTodayOrdersGrowth(100.0); // 如果昨天没有订单，则增长率为100%
        }
        
        if (yesterdayRevenue != null && yesterdayRevenue.compareTo(BigDecimal.ZERO) > 0) {
            double revenueGrowth = (todayRevenue.subtract(yesterdayRevenue))
                    .divide(yesterdayRevenue, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"))
                    .doubleValue();
            stats.setTodayRevenueGrowth(Math.round(revenueGrowth * 10) / 10.0);
        } else {
            stats.setTodayRevenueGrowth(100.0); // 如果昨天没有收入，则增长率为100%
        }
        
        // 获取其他统计
        Long totalCustomers = merchantStatsMapper.getTotalCustomers(merchantId);
        stats.setTotalCustomers(totalCustomers != null ? totalCustomers.intValue() : 0);
        
        // 获取平均评分并保留一位小数
        Double avgRating = merchantStatsMapper.getAvgRating(merchantId);
        if (avgRating != null) {
            stats.setAvgRating(Math.round(avgRating * 10) / 10.0);
        } else {
            stats.setAvgRating(5.0); // 默认评分为5.0
        }
        
        Long pendingOrders = merchantStatsMapper.getPendingOrders(merchantId);
        stats.setPendingOrders(pendingOrders != null ? pendingOrders.intValue() : 0);
        stats.setTotalSales(merchantStatsMapper.getTotalSales(merchantId));
        
        // 获取销售趋势数据（默认为本周）
        stats.setSalesTrend(getSalesTrend(merchantId, "week"));
        
        // 获取菜品分类销售量统计
        stats.setCategorySales(getCategorySales(merchantId));
        
        return stats;
    }
    
    @Override
    public List<SalesTrendDTO> getSalesTrend(Long merchantId, String type) {
        List<Map<String, Object>> trendData;
        if ("month".equals(type)) {
            trendData = merchantStatsMapper.getMonthSalesTrend(merchantId);
        } else {
            trendData = merchantStatsMapper.getWeekSalesTrend(merchantId);
        }
        
        List<SalesTrendDTO> result = new ArrayList<>();
        for (Map<String, Object> data : trendData) {
            SalesTrendDTO dto = new SalesTrendDTO();
            dto.setDate((String) data.get("date"));
            dto.setAmount((BigDecimal) data.get("amount"));
            result.add(dto);
        }
        
        return result;
    }
    
    @Override
    public List<CategorySalesDTO> getCategorySales(Long merchantId) {
        List<Map<String, Object>> salesData = merchantStatsMapper.getCategorySales(merchantId);
        
        List<CategorySalesDTO> result = new ArrayList<>();
        for (Map<String, Object> data : salesData) {
            CategorySalesDTO dto = new CategorySalesDTO();
            dto.setCategory((String) data.get("category"));
            dto.setCount(((Number) data.get("count")).intValue());
            result.add(dto);
        }
        
        return result;
    }
}