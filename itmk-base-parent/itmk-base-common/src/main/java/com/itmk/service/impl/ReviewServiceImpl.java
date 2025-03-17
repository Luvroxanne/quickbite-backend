package com.itmk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itmk.dto.ReviewRequestDTO;
import com.itmk.entity.Review;
import com.itmk.exception.BusinessException;
import com.itmk.mapper.ReviewMapper;
import com.itmk.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {

    @Override
    public List<Review> getCustomerReviews(Long customerId) {
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Review::getCustomerId, customerId)
                .orderByDesc(Review::getCreatedAt);
        return this.list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addReview(ReviewRequestDTO reviewRequest, Long customerId) {
        try {
            Review review = new Review();
            review.setOrderId(reviewRequest.getOrderId());
            review.setCustomerId(customerId);
            review.setMerchantId(reviewRequest.getMerchantId());
            review.setRating(reviewRequest.getRating());
            review.setContent(reviewRequest.getContent());
            review.setImages(String.join(",", reviewRequest.getImages()));
            review.setStatus("pending");
            review.setCreatedAt(new Date());
            review.setUpdatedAt(new Date());
            
            return this.save(review);
        } catch (Exception e) {
            log.error("添加评论失败", e);
            throw new BusinessException("添加评论失败: " + e.getMessage());
        }
    }

    @Override
    public List<Review> getMerchantReviews(Long merchantId) {
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Review::getMerchantId, merchantId)
                .orderByAsc(Review::getStatus)
                .orderByDesc(Review::getCreatedAt);
        return this.list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean replyReview(Long reviewId, String reply, Long merchantId) {
        try {
            Review review = this.getById(reviewId);
            if (review == null) {
                throw new BusinessException("评论不存在");
            }
            
            if (!review.getMerchantId().equals(merchantId)) {
                throw new BusinessException("无权回复此评论");
            }
            
            review.setReply(reply);
            review.setReplyTime(new Date());
            review.setStatus("replied");
            review.setUpdatedAt(new Date());
            
            return this.updateById(review);
        } catch (Exception e) {
            log.error("回复评论失败", e);
            throw new BusinessException("回复评论失败: " + e.getMessage());
        }
    }

    @Override
    public boolean existsByOrderId(Long orderId) {
        return this.lambdaQuery()
                .eq(Review::getOrderId, orderId)
                .exists();
    }
}