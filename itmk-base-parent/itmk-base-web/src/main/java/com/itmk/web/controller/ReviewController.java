package com.itmk.web.controller;

import com.itmk.annotation.RequireRole;
import com.itmk.constant.SystemConstants;
import com.itmk.dto.ReviewRequestDTO;
import com.itmk.dto.ReviewReplyDTO;
import com.itmk.entity.Review;
import com.itmk.result.ResultVo;
import com.itmk.service.ReviewService;
import com.itmk.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Api(tags = "评论管理")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @ApiOperation("顾客获取评论列表")
    @GetMapping("/customer/list")
    @RequireRole(SystemConstants.ROLE_CUSTOMER)
    public ResultVo<?> getCustomerReviews() {
        try {
            Long customerId = UserContext.getCurrentUserId();
            List<Review> reviews = reviewService.getCustomerReviews(customerId);
            return ResultVo.success(reviews);
        } catch (Exception e) {
            log.error("获取顾客评论列表失败", e);
            return ResultVo.fail(e.getMessage());
        }
    }

    @ApiOperation("顾客添加评论")
    @PostMapping("/add")
    @RequireRole(SystemConstants.ROLE_CUSTOMER)
    public ResultVo<?> addReview(@RequestBody @Valid ReviewRequestDTO reviewRequest) {
        try {
            Long customerId = UserContext.getCurrentUserId();
            boolean success = reviewService.addReview(reviewRequest, customerId);
            return success ? ResultVo.success("评论成功") : ResultVo.fail("评论失败");
        } catch (Exception e) {
            log.error("添加评论失败", e);
            return ResultVo.fail(e.getMessage());
        }
    }

    @ApiOperation("商家获取评论列表")
    @GetMapping("/merchant/list")
    @RequireRole(SystemConstants.ROLE_MERCHANT)
    public ResultVo<?> getMerchantReviews() {
        try {
            Long merchantId = UserContext.getCurrentUserId();
            List<Review> reviews = reviewService.getMerchantReviews(merchantId);
            return ResultVo.success(reviews);
        } catch (Exception e) {
            log.error("获取商家评论列表失败", e);
            return ResultVo.fail(e.getMessage());
        }
    }

    @ApiOperation("商家回复评论")
    @PutMapping("/{id}/reply")
    @RequireRole(SystemConstants.ROLE_MERCHANT)
    public ResultVo<?> replyReview(
            @PathVariable Long id,
            @RequestBody @Valid ReviewReplyDTO replyRequest) {
        try {
            Long merchantId = UserContext.getCurrentUserId();
            boolean success = reviewService.replyReview(id, replyRequest.getReply(), merchantId);
            return success ? ResultVo.success("回复成功") : ResultVo.fail("回复失败");
        } catch (Exception e) {
            log.error("回复评论失败", e);
            return ResultVo.fail(e.getMessage());
        }
    }
}