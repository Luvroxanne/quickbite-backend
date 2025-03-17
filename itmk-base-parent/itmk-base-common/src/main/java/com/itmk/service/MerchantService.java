package com.itmk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itmk.entity.Merchant;

/**
 * @Author: MK
 * @Description: 商家服务接口
 * @Date: 2024/3/20
 */
public interface MerchantService extends IService<Merchant> {
    /**
     * 创建商家信息
     * @param merchant 商家信息
     * @return 是否创建成功
     */
    boolean createMerchant(Merchant merchant);

    /**
     * 根据用户ID获取商家信息
     */
    Merchant getByUserId(Long userId);
} 