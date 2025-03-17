package com.itmk.web.controller;

import com.itmk.result.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.Map;

import com.itmk.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import com.itmk.exception.BusinessException;
import com.itmk.service.UploadService;

@Api(tags = "文件上传")
@Slf4j
@RestController
@RequestMapping("/api/upload")
public class UploadController {
    
    @Value("${file.upload.path}")
    private String uploadPath;
    
    @Value("${file.upload.url-prefix}")
    private String urlPrefix;
    
    @Autowired
    private UploadService uploadService;
    
    @ApiOperation("上传图片")
    @PostMapping("/image")
    public ResultVo<Map<String, String>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type) {
        log.info("开始上传图片，类型: {}", type);
        try {
            // 检查文件类型
            String contentType = file.getContentType();
            if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
                return ResultVo.fail("只支持JPG/PNG格式图片");
            }
            
            // 检查文件大小（2MB）
            if (file.getSize() > 2 * 1024 * 1024) {
                return ResultVo.fail("图片大小不能超过2MB");
            }
            
            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                return ResultVo.fail("文件名不能为空");
            }
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString() + extension;
            
            // 构建年月日目录
            String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            String relativePath = "images/" + datePath + "/";
            String absolutePath = uploadPath + relativePath;
            
            // 创建目录
            File directory = new File(absolutePath);
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    return ResultVo.fail("创建目录失败");
                }
            }
            
            // 保存文件
            File destFile = new File(absolutePath + fileName);
            file.transferTo(destFile);
            
            // 返回可访问的URL
            String fileUrl = urlPrefix + relativePath + fileName;
            
            // 获取当前用户ID
            Long userId = UserContext.getCurrentUserId();
            
            // 根据类型更新不同的图片
            if ("avatar".equals(type)) {
                log.info("更新用户头像");
                return ResultVo.success(uploadService.updateUserAvatar(userId, fileUrl));
            } else if ("logo".equals(type)) {
                log.info("更新商家logo");
                return ResultVo.success(uploadService.updateMerchantLogo(userId, fileUrl));
            } else if ("dish".equals(type)) {
                log.info("上传菜品图片");
                return ResultVo.success(uploadService.uploadDishImage(userId, fileUrl));
            } else if ("comment".equals(type)) {
                log.info("上传评论图片");
                return ResultVo.success(uploadService.uploadCommentImage(userId, fileUrl));
            } else {
                return ResultVo.fail("无效的上传类型，支持的类型：avatar/logo/dish/comment");
            }
            
        } catch (BusinessException e) {
            log.error("业务异常: {}", e.getMessage());
            return ResultVo.fail(e.getMessage());
        } catch (Exception e) {
            log.error("文件上传失败", e);
            return ResultVo.fail("文件上传失败: " + e.getMessage());
        }
    }
}