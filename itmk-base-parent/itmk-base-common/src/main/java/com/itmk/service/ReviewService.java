package com.itmk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itmk.dto.ReviewRequestDTO;
import com.itmk.entity.Review;
import java.util.List;

public interface ReviewService extends IService<Review> {
    /**
     * 获取顾客的评论列表
     */
    List<Review> getCustomerReviews(Long customerId);

    /**
     * 添加评论
     */
    boolean addReview(ReviewRequestDTO reviewRequest, Long customerId);

    /**
     * 获取商家的评论列表
     */
    List<Review> getMerchantReviews(Long merchantId);

    /**
     * 商家回复评论
     */
    boolean replyReview(Long reviewId, String reply, Long merchantId);

    /**
     * 检查订单是否已评价
     */
    default boolean existsByOrderId(Long orderId) {
        return this.lambdaQuery()
                .eq(Review::getOrderId, orderId)
                .exists();
    }
}