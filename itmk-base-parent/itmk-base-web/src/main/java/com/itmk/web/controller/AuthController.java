package com.itmk.web.controller;

import com.itmk.dto.LoginRequestDTO;
import com.itmk.dto.RegisterRequestDTO;
import com.itmk.dto.UpdatePasswordDTO;
import com.itmk.dto.UserInfoDTO;
import com.itmk.dto.UpdateUserInfoDTO;
import com.itmk.entity.User;
import com.itmk.exception.BusinessException;
import com.itmk.result.ResultVo;
import com.itmk.service.UserService;
import com.itmk.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Map;

/**
 * @Author: MK
 * @Description: 认证控制器
 * @Date: 2024/3/20
 */
@Slf4j
@Api(tags = "认证管理")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public ResultVo<?> login(@RequestBody LoginRequestDTO loginRequest) {
        // 1. 调用登录服务
        Map<String, Object> result = userService.login(loginRequest);
        
        // 2. 获取用户角色
        User user = (User) result.get("user");
        
        // 3. 验证用户状态
        if (user.getStatus() != 1) {
            throw new BusinessException("账号已被禁用，请联系管理员");
        }
        
        // 4. 验证用户角色与请求角色是否匹配
        String requestRole = loginRequest.getUserType();
        if (requestRole != null && !requestRole.equals(user.getRole())) {
            throw new BusinessException("非" + getRoleName(requestRole) + "账号不能登录");
        }
        
        return ResultVo.success(result);
    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public ResultVo<?> register(@RequestBody @Valid RegisterRequestDTO registerRequest) {
        if (userService.checkUsernameExists(registerRequest.getUsername())) {
            return ResultVo.fail("用户名已存在");
        }
        return ResultVo.success(userService.register(registerRequest));
    }

    @ApiOperation("修改密码")
    @PutMapping("/password")
    public ResultVo<String> updatePassword(@RequestBody @Valid UpdatePasswordDTO passwordDTO) {
        // 获取当前用户
        User user = userService.getById(UserContext.getCurrentUserId());
        if (user == null) {
            return ResultVo.fail("用户不存在");
        }
        
        // 验证原密码
        if (!user.getPassword().equals(passwordDTO.getOldPassword())) {
            return ResultVo.fail("原密码不正确");
        }
        
        // 更新密码
        user.setPassword(passwordDTO.getNewPassword());
        return userService.updateById(user) ? 
            ResultVo.success("密码修改成功") : 
            ResultVo.fail("密码修改失败");
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public ResultVo<UserInfoDTO> getUserInfo() {
        // 获取当前用户ID
        Long userId = UserContext.getCurrentUserId();
        
        // 获取用户信息
        User user = userService.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 转换为DTO
        UserInfoDTO userInfo = new UserInfoDTO();
        BeanUtils.copyProperties(user, userInfo);
        
        return ResultVo.success(userInfo);
    }

    @ApiOperation("更新用户信息")
    @PutMapping("/info")
    public ResultVo<UserInfoDTO> updateUserInfo(@RequestBody @Valid UpdateUserInfoDTO updateDTO) {
        // 获取当前用户ID
        Long userId = UserContext.getCurrentUserId();
        
        // 获取用户信息
        User user = userService.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 更新用户基本信息
        user.setPhone(updateDTO.getPhone());
        user.setEmail(updateDTO.getEmail());
        
        // 只有当明确要更新头像时才更新avatar字段
        if (updateDTO.getAvatar() != null && !updateDTO.getAvatar().isEmpty()) {
            user.setAvatar(updateDTO.getAvatar());
        }
        
        user.setUpdatedAt(new Date());
        
        // 保存更新
        if (!userService.updateById(user)) {
            throw new BusinessException("更新用户信息失败");
        }
        
        // 转换为DTO并返回
        UserInfoDTO userInfo = new UserInfoDTO();
        BeanUtils.copyProperties(user, userInfo);
        
        return ResultVo.success(userInfo);
    }

    @ApiOperation("管理员重置用户密码")
    @PutMapping("/admin/reset-password/{userId}")
    public ResultVo<String> resetUserPassword(@PathVariable Long userId, @RequestParam String newPassword) {
        // 检查当前用户是否为管理员
        Long adminId = UserContext.getCurrentUserId();
        User admin = userService.getById(adminId);
        if (admin == null || !"admin".equals(admin.getRole())) {
            return ResultVo.fail("无权操作");
        }
        
        // 获取目标用户
        User user = userService.getById(userId);
        if (user == null) {
            return ResultVo.fail("用户不存在");
        }
        
        // 更新密码
        user.setPassword(newPassword);
        return userService.updateById(user) ? 
            ResultVo.success("密码重置成功") : 
            ResultVo.fail("密码重置失败");
    }

    @ApiOperation("管理员修改用户状态")
    @PutMapping("/admin/status/{userId}")
    public ResultVo<String> updateUserStatus(@PathVariable Long userId, @RequestParam Integer status) {
        // 检查当前用户是否为管理员
        Long adminId = UserContext.getCurrentUserId();
        User admin = userService.getById(adminId);
        if (admin == null || !"admin".equals(admin.getRole())) {
            return ResultVo.fail("无权操作");
        }
        
        // 获取目标用户
        User user = userService.getById(userId);
        if (user == null) {
            return ResultVo.fail("用户不存在");
        }
        
        // 状态只能是0或1
        if (status != 0 && status != 1) {
            return ResultVo.fail("状态值无效");
        }
        
        // 更新状态
        user.setStatus(status);
        return userService.updateById(user) ? 
            ResultVo.success(status == 1 ? "账号已启用" : "账号已禁用") : 
            ResultVo.fail("状态更新失败");
    }

    @ApiOperation("管理员获取用户列表")
    @GetMapping("/admin/users")
    public ResultVo<?> getUserList(@RequestParam(required = false) String role, 
                                  @RequestParam(required = false) String keyword) {
        // 检查当前用户是否为管理员
        Long adminId = UserContext.getCurrentUserId();
        User admin = userService.getById(adminId);
        if (admin == null || !"admin".equals(admin.getRole())) {
            return ResultVo.fail("无权操作");
        }
        
        // 调用服务获取用户列表
        return ResultVo.success(userService.getUserList(role, keyword));
    }

    /**
     * 获取角色名称
     */
    private String getRoleName(String role) {
        switch (role) {
            case "admin":
                return "管理员";
            case "merchant":
                return "商家";
            case "customer":
                return "顾客";
            case "rider":
                return "骑手";
            default:
                return "未知角色";
        }
    }
}