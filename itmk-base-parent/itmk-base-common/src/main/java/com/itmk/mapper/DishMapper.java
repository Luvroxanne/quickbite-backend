package com.itmk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itmk.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: MK
 * @Description: 菜品Mapper接口
 * @Date: 2024/3/20
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
} 