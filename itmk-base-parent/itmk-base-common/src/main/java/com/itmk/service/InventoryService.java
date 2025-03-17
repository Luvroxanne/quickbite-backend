package com.itmk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itmk.entity.Inventory;
import java.util.List;

public interface InventoryService extends IService<Inventory> {
    /**
     * 获取商家的库存列表
     */
    List<Inventory> getInventoryList(Long merchantId);

    /**
     * 删除库存
     */
    boolean deleteInventory(Long id, Long merchantId);

    /**
     * 更新库存状态
     */
    void updateStatus(Inventory inventory);
}