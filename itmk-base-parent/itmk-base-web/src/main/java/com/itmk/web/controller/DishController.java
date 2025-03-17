package com.itmk.web.controller;

import com.itmk.entity.Dish;
import com.itmk.entity.Merchant;
import com.itmk.result.ResultVo;
import com.itmk.service.DishService;
import com.itmk.service.MerchantService;
import com.itmk.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Date;

/**
 * @Author: MK
 * @Description: 菜品控制器
 * @Date: 2024/3/20
 */
@Api(tags = "菜品管理")
@RestController
@Slf4j
@RequestMapping("/api/menu")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private MerchantService merchantService;

    @ApiOperation("获取菜品列表")
    @GetMapping("/list")
    public ResultVo<?> list(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) Boolean showDeleted) {
        // 1. 获取当前登录用户ID
        Long userId = UserContext.getCurrentUserId();

        // 2. 通过用户ID获取商家信息
        Merchant merchant = merchantService.getByUserId(userId);
        if (merchant == null) {
            return ResultVo.fail("商家信息不存在");
        }

        // 3. 使用商家ID查询菜品列表
        List<Dish> result = dishService.listDishes(category, status, merchant.getId(), showDeleted);
        return ResultVo.success(result);
    }

    @ApiOperation("添加菜品")
    @PostMapping("/add")
    public ResultVo<?> add(@RequestBody @Valid Dish dish) {
        // 1. 获取当前商家ID
        Long userId = UserContext.getCurrentUserId();
        Merchant merchant = merchantService.getByUserId(userId);
        if (merchant == null) {
            return ResultVo.fail("商家信息不存在");
        }
        
        // 2. 设置商家ID
        dish.setMerchantId(merchant.getId());
        dish.setCreatedAt(new Date());
        dish.setUpdatedAt(new Date());
        
        // 3. 添加菜品
        return ResultVo.success(dishService.save(dish));
    }

    @ApiOperation("更新菜品")
    @PutMapping("/{id}")
    public ResultVo<?> update(@PathVariable Long id, @RequestBody @Valid Dish dish) {
        // 1. 获取当前商家ID
        Long userId = UserContext.getCurrentUserId();
        Merchant merchant = merchantService.getByUserId(userId);
        if (merchant == null) {
            return ResultVo.fail("商家信息不存在");
        }
        
        // 2. 验证菜品是否属于当前商家
        Dish existDish = dishService.getById(id);
        if (existDish == null || !existDish.getMerchantId().equals(merchant.getId())) {
            return ResultVo.fail("菜品不存在或无权限修改");
        }
        
        // 3. 设置ID和商家ID
        dish.setId(id);
        dish.setMerchantId(merchant.getId());
        dish.setUpdatedAt(new Date());
        
        // 4. 更新菜品
        return ResultVo.success(dishService.updateById(dish));
    }

    @ApiOperation("更新菜品状态")
    @PutMapping("/{id}/status")
    public ResultVo<?> updateStatus(@PathVariable Long id, @RequestBody Dish dish) {
        // 1. 获取当前商家ID
        Long userId = UserContext.getCurrentUserId();
        Merchant merchant = merchantService.getByUserId(userId);
        if (merchant == null) {
            return ResultVo.fail("商家信息不存在");
        }
        
        // 2. 验证菜品是否属于当前商家
        Dish existDish = dishService.getById(id);
        if (existDish == null || !existDish.getMerchantId().equals(merchant.getId())) {
            return ResultVo.fail("菜品不存在或无权限修改");
        }
        
        // 3. 更新菜品状态
        existDish.setStatus(dish.getStatus());
        existDish.setUpdatedAt(new Date());
        return ResultVo.success(dishService.updateById(existDish));
    }

    @ApiOperation("批量删除菜品")
        @DeleteMapping("/delete")
        public ResultVo<?> deleteBatch(@RequestBody List<Long> ids) {
            try {
                // 1. 获取当前商家ID
                Long userId = UserContext.getCurrentUserId();
                Merchant merchant = merchantService.getByUserId(userId);
                if (merchant == null) {
                    return ResultVo.fail("商家信息不存在");
                }
    
                // 2. 执行批量删除
                boolean result = dishService.deleteBatch(ids, merchant.getId());
                if (result) {
                    return ResultVo.success("删除成功");
                } else {
                    return ResultVo.fail("删除失败");
                }
            } catch (Exception e) {
                log.error("删除菜品失败", e);
                return ResultVo.fail("删除失败：" + e.getMessage());
            }
        }
}