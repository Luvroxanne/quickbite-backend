package com.itmk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itmk.entity.Delivery;
import java.util.List;
import java.util.Map;

public interface DeliveryService extends IService<Delivery> {
    /**
     * 获取配送订单列表（包含订单详情）
     */
    List<Delivery> getDeliveryList(Long merchantId);

    /**
     * 获取配送统计信息
     */
    Map<String, Object> getDeliveryStats(Long merchantId);

    /**
     * 获取在线骑手列表
     */
    List<Map<String, Object>> getOnlineRiders(Long merchantId);
}
