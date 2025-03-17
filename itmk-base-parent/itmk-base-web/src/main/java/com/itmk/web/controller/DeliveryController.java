package com.itmk.web.controller;

import com.itmk.annotation.RequireRole;
import com.itmk.constant.SystemConstants;
import com.itmk.entity.Delivery;
import com.itmk.result.ResultVo;
import com.itmk.service.DeliveryService;
import com.itmk.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = "配送管理")
@RestController
@RequestMapping("/api/delivery")
@RequireRole(SystemConstants.ROLE_MERCHANT)
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @ApiOperation("获取配送订单列表")
    @GetMapping("/list")
    public ResultVo<?> getDeliveryList() {
        try {
            Long merchantId = UserContext.getCurrentUserId();
            List<Delivery> list = deliveryService.getDeliveryList(merchantId);
            return ResultVo.success(list);
        } catch (Exception e) {
            log.error("获取配送订单列表失败", e);
            return ResultVo.fail("获取配送订单列表失败");
        }
    }

    @ApiOperation("获取配送统计信息")
    @GetMapping("/stats")
    public ResultVo<?> getDeliveryStats() {
        try {
            Long merchantId = UserContext.getCurrentUserId();
            Map<String, Object> stats = deliveryService.getDeliveryStats(merchantId);
            return ResultVo.success(stats);
        } catch (Exception e) {
            log.error("获取配送统计信息失败", e);
            return ResultVo.fail("获取配送统计信息失败");
        }
    }

    @ApiOperation("获取在线骑手列表")
    @GetMapping("/riders")
    public ResultVo<?> getOnlineRiders() {
        try {
            Long merchantId = UserContext.getCurrentUserId();
            return ResultVo.success(deliveryService.getOnlineRiders(merchantId));
        } catch (Exception e) {
            log.error("获取在线骑手列表失败", e);
            return ResultVo.fail("获取在线骑手列表失败");
        }
    }
}
