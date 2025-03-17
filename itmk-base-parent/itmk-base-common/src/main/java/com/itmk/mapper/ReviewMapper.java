package com.itmk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itmk.entity.Review;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReviewMapper extends BaseMapper<Review> {
    // 继承MyBatis-Plus的BaseMapper接口，自动实现基础的CRUD方法
}