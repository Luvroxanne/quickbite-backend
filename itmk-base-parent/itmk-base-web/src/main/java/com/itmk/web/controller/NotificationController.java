package com.itmk.web.controller;

import com.itmk.entity.Notification;
import com.itmk.result.ResultVo;
import com.itmk.service.NotificationService;
import com.itmk.utils.UserContext;
import com.itmk.dto.MarkReadDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api(tags = "通知管理")
@RestController
@RequestMapping("/api/merchant/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @ApiOperation("获取通知列表")
    @GetMapping
    public ResultVo<List<Notification>> list() {
        Long userId = UserContext.getCurrentUserId();
        return ResultVo.success(notificationService.getUserNotifications(userId));
    }

    @ApiOperation("标记通知为已读")
    @PutMapping("/read")
    public ResultVo<?> markAsRead(@RequestBody MarkReadDTO markReadDTO) {
        Long userId = UserContext.getCurrentUserId();
        return ResultVo.success(notificationService.markAsRead(markReadDTO, userId));
    }
} 