package com.itmk.web.controller;

import com.itmk.annotation.RequireRole;
import com.itmk.constant.SystemConstants;
import com.itmk.entity.Inventory;
import com.itmk.result.ResultVo;
import com.itmk.service.InventoryService;
import com.itmk.utils.UserContext;
import com.itmk.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Slf4j
@Api(tags = "库存管理")
@RestController
@RequestMapping("/api/inventory")
@RequireRole(SystemConstants.ROLE_MERCHANT)
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @ApiOperation("获取库存列表")
    @GetMapping
    public ResultVo<?> list() {
        try {
            Long merchantId = UserContext.getCurrentUserId();
            List<Inventory> list = inventoryService.getInventoryList(merchantId);
            return ResultVo.success(list);
        } catch (BusinessException e) {
            log.error("获取库存列表失败", e);
            return ResultVo.fail(e.getMessage());
        }
    }

    @ApiOperation("添加库存")
    @PostMapping
    public ResultVo<?> add(@RequestBody @Valid Inventory inventory) {
        try {
            Long merchantId = UserContext.getCurrentUserId();
            inventory.setMerchantId(merchantId);
            inventory.setCreatedAt(new Date());
            inventory.setUpdatedAt(new Date());
            
            // 根据库存量和警戒库存设置状态
            inventoryService.updateStatus(inventory);
            
            boolean success = inventoryService.save(inventory);
            return success ? ResultVo.success("添加成功") : ResultVo.fail("添加失败");
        } catch (BusinessException e) {
            log.error("添加库存失败", e);
            return ResultVo.fail(e.getMessage());
        }
    }

    @ApiOperation("更新库存")
    @PutMapping("/{id}")
    public ResultVo<?> update(@PathVariable Long id, @RequestBody @Valid Inventory inventory) {
        try {
            Long merchantId = UserContext.getCurrentUserId();
            Inventory existInventory = inventoryService.getById(id);
            
            if (existInventory == null || !existInventory.getMerchantId().equals(merchantId)) {
                return ResultVo.fail("库存不存在或无权操作");
            }
            
            existInventory.setStock(inventory.getStock());
            existInventory.setWarningLevel(inventory.getWarningLevel());
            existInventory.setUpdatedAt(new Date());
            
            // 更新状态
            inventoryService.updateStatus(existInventory);
            
            boolean success = inventoryService.updateById(existInventory);
            return success ? ResultVo.success("更新成功") : ResultVo.fail("更新失败");
        } catch (BusinessException e) {
            log.error("更新库存失败", e);
            return ResultVo.fail(e.getMessage());
        }
    }

    @ApiOperation("删除库存")
    @DeleteMapping("/{id}")
    public ResultVo<?> delete(@PathVariable Long id) {
        try {
            Long merchantId = UserContext.getCurrentUserId();
            boolean success = inventoryService.deleteInventory(id, merchantId);
            return success ? ResultVo.success("删除成功") : ResultVo.fail("删除失败");
        } catch (BusinessException e) {
            log.error("删除库存失败", e);
            return ResultVo.fail(e.getMessage());
        }
    }
}