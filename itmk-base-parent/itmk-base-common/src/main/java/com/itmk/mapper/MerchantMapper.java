package com.itmk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itmk.entity.Merchant;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: MK
 * @Description: 商家Mapper接口
 * @Date: 2024/3/20
 */
@Mapper
public interface MerchantMapper extends BaseMapper<Merchant> {
} 