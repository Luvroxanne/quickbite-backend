package com.itmk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itmk.entity.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: MK
 * @Description: 订单Mapper接口
 * @Date: 2024/3/20
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
} 