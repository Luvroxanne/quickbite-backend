package com.itmk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itmk.entity.Inventory;
import com.itmk.exception.BusinessException;
import com.itmk.mapper.InventoryMapper;
import com.itmk.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements InventoryService {

    @Override
    public List<Inventory> getInventoryList(Long merchantId) {
        try {
            LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Inventory::getMerchantId, merchantId)
                    .orderByDesc(Inventory::getUpdatedAt);
            return this.list(wrapper);
        } catch (Exception e) {
            log.error("获取库存列表失败", e);
            throw new BusinessException("获取库存列表失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteInventory(Long id, Long merchantId) {
        try {
            return this.remove(new LambdaQueryWrapper<Inventory>()
                    .eq(Inventory::getId, id)
                    .eq(Inventory::getMerchantId, merchantId));
        } catch (Exception e) {
            log.error("删除库存失败", e);
            throw new BusinessException("删除库存失败: " + e.getMessage());
        }
    }

    @Override
    public void updateStatus(Inventory inventory) {
        try {
            int stock = inventory.getStock();
            int warningLevel = inventory.getWarningLevel();
            
            if (stock > warningLevel) {
                inventory.setStatus("normal");
            } else if (stock < warningLevel) {
                inventory.setStatus("low");
            } else if (stock >= (warningLevel - 5) && stock <= (warningLevel + 5)) {
                inventory.setStatus("warning");
            }
        } catch (Exception e) {
            log.error("更新库存状态失败", e);
            throw new BusinessException("更新库存状态失败: " + e.getMessage());
        }
    }
}