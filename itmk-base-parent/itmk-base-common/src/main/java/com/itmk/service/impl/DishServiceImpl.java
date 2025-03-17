package com.itmk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itmk.entity.Dish;
import com.itmk.exception.BusinessException;
import com.itmk.mapper.DishMapper;
import com.itmk.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

/**
 * @Author: MK
 * @Description: 菜品服务实现类
 * @Date: 2024/3/20
 */
@Slf4j
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Override
    public List<Dish> listDishes(String category, Boolean status, Long merchantId, Boolean showDeleted) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        
        // 商家 ID 必填
        wrapper.eq(Dish::getMerchantId, merchantId);
        
        // 分类（去除首尾空格）
        if (StringUtils.hasText(category)) {
            wrapper.eq(Dish::getCategory, category.trim());
        }
        
        // 状态字段（避免 Boolean 直接传入）
        if (status != null) {
            wrapper.eq(Dish::getStatus, status ? 1 : 0);
        }
        
        // 逻辑删除字段（Boolean 转换为 Integer）
        wrapper.eq(Dish::getDeleted, Boolean.TRUE.equals(showDeleted) ? 1 : 0);
        
        // 按创建时间降序排序
        wrapper.orderByDesc(Dish::getCreatedAt);
        
        // 确保返回非空列表
        return Optional.ofNullable(this.list(wrapper)).orElse(Collections.emptyList());
    }
    

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addDish(Dish dish) {
        dish.setCreatedAt(new Date());
        dish.setUpdatedAt(new Date());
        dish.setSales(0);
        
        return this.save(dish);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDish(Long id, Dish dish) {
        Dish existDish = this.getById(id);
        if (existDish == null) {
            throw new BusinessException("菜品不存在");
        }
        
        dish.setId(id);
        dish.setUpdatedAt(new Date());
        
        return this.updateById(dish);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long id, Boolean status) {
        Dish dish = new Dish();
        dish.setId(id);
        dish.setStatus(status);
        dish.setUpdatedAt(new Date());
        
        return this.updateById(dish);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(List<Long> ids, Long merchantId) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        // 1. 验证所有菜品是否属于当前商家
        List<Dish> dishes = this.listByIds(ids);
        for (Dish dish : dishes) {
            if (!dish.getMerchantId().equals(merchantId)) {
                throw new BusinessException("存在无权操作的菜品");
            }
        }

        // 2. 批量更新删除标志
        return this.update()
                .set("deleted", 1)
                .set("updated_at", new Date())
                .in("id", ids)
                .eq("merchant_id", merchantId)
                .update();
    }
}