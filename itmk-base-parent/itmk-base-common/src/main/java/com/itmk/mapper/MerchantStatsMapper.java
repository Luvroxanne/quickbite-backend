package com.itmk.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface MerchantStatsMapper {
    @Select("SELECT COUNT(*) as todayOrders, COALESCE(SUM(total_amount), 0) as todayRevenue " +
            "FROM orders WHERE merchant_id = #{merchantId} AND DATE(created_at) = CURDATE()")
    Map<String, Object> getTodayStats(@Param("merchantId") Long merchantId);

    @Select("SELECT COUNT(*) as yesterdayOrders FROM orders " +
            "WHERE merchant_id = #{merchantId} AND DATE(created_at) = DATE_SUB(CURDATE(), INTERVAL 1 DAY)")
    Long getYesterdayOrders(@Param("merchantId") Long merchantId);

    @Select("SELECT COALESCE(SUM(total_amount), 0) as yesterdayRevenue FROM orders " +
            "WHERE merchant_id = #{merchantId} AND DATE(created_at) = DATE_SUB(CURDATE(), INTERVAL 1 DAY)")
    BigDecimal getYesterdayRevenue(@Param("merchantId") Long merchantId);

    @Select("SELECT COUNT(DISTINCT customer_id) as totalCustomers FROM orders WHERE merchant_id = #{merchantId}")
    Long getTotalCustomers(@Param("merchantId") Long merchantId);

    @Select("SELECT COALESCE(AVG(rating), 0.0) as avgRating FROM reviews WHERE merchant_id = #{merchantId}")
    Double getAvgRating(@Param("merchantId") Long merchantId);

    @Select("SELECT COUNT(*) as pendingOrders FROM orders " +
            "WHERE merchant_id = #{merchantId} AND status = 'preparing'")
    Long getPendingOrders(@Param("merchantId") Long merchantId);

    @Select("SELECT COALESCE(SUM(total_amount), 0) as totalSales FROM orders " +
            "WHERE merchant_id = #{merchantId} AND status = 'completed'")
    BigDecimal getTotalSales(@Param("merchantId") Long merchantId);
    
    /**
     * 获取本周（近7天）销售趋势数据
     */
    @Select("SELECT DATE_FORMAT(created_at, '%m-%d') as date, COALESCE(SUM(total_amount), 0) as amount " +
            "FROM orders " +
            "WHERE merchant_id = #{merchantId} " +
            "AND created_at >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) " +
            "AND created_at <= CURDATE() " +
            "GROUP BY DATE(created_at) " +
            "ORDER BY DATE(created_at)")
    List<Map<String, Object>> getWeekSalesTrend(@Param("merchantId") Long merchantId);
    
    /**
     * 获取本月（近7个月）销售趋势数据
     */
    @Select("SELECT DATE_FORMAT(created_at, '%Y-%m') as date, COALESCE(SUM(total_amount), 0) as amount " +
            "FROM orders " +
            "WHERE merchant_id = #{merchantId} " +
            "AND created_at >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH) " +
            "AND created_at <= CURDATE() " +
            "GROUP BY DATE_FORMAT(created_at, '%Y-%m') " +
            "ORDER BY DATE_FORMAT(created_at, '%Y-%m')")
    List<Map<String, Object>> getMonthSalesTrend(@Param("merchantId") Long merchantId);
    
    /**
     * 获取菜品分类销售量统计
     */
    @Select("SELECT d.category, COALESCE(SUM(oi.quantity), 0) as count " +
            "FROM order_items oi " +
            "JOIN orders o ON oi.order_id = o.id " +
            "JOIN dishes d ON oi.dish_id = d.id " +
            "WHERE o.merchant_id = #{merchantId} " +
            "GROUP BY d.category " +
            "ORDER BY count DESC")
    List<Map<String, Object>> getCategorySales(@Param("merchantId") Long merchantId);
}