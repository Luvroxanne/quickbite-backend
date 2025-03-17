package com.itmk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itmk.entity.Merchant;
import com.itmk.mapper.MerchantMapper;
import com.itmk.service.MerchantService;
import org.springframework.stereotype.Service;

/**
 * @Author: MK
 * @Description: 商家服务实现类
 * @Date: 2024/3/20
 */
@Service
public class MerchantServiceImpl extends ServiceImpl<MerchantMapper, Merchant> implements MerchantService {

    @Override
    public boolean createMerchant(Merchant merchant) {
        return this.save(merchant);
    }

    @Override
    public Merchant getByUserId(Long userId) {
        LambdaQueryWrapper<Merchant> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Merchant::getUserId, userId);
        return this.getOne(wrapper);
    }
} 