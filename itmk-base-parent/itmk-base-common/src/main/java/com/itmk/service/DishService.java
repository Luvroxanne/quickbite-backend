package com.itmk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itmk.entity.Dish;
import java.util.List;

/**
 * @Author: MK
 * @Description: 菜品服务接口
 * @Date: 2024/3/20
 */
public interface DishService extends IService<Dish> {
    /**
     * 获取商家的菜品列表
     * @param category 分类
     * @param status 状态
     * @param merchantId 商家ID
     * @param showDeleted 是否显示已删除
     * @return 菜品列表
     */
    List<Dish> listDishes(String category, Boolean status, Long merchantId, Boolean showDeleted);
    /**
     * 添加菜品
     */
    boolean addDish(Dish dish);

    /**
     * 更新菜品
     */
    boolean updateDish(Long id, Dish dish);

    /**
     * 更新菜品状态
     */
    boolean updateStatus(Long id, Boolean status);

    /**
     * 批量逻辑删除菜品（支持单个和多个）
     * @param ids 菜品ID列表
     * @param merchantId 商家ID
     * @return 是否删除成功
     */
    boolean deleteBatch(List<Long> ids, Long merchantId);
}