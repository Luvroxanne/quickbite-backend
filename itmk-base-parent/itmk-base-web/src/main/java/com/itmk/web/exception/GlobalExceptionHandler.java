package com.itmk.web.exception;

import com.itmk.exception.BusinessException;
import com.itmk.result.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: MK
 * @Description: 全局异常处理器
 * @Date: 2024/3/20
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResultVo<?> handleBusinessException(BusinessException e) {
        log.error("业务异常：{}", e.getMessage());
        return ResultVo.fail(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResultVo<?> handleException(Exception e) {
        log.error("系统异常：", e);
        return ResultVo.fail("系统异常，请联系管理员");
    }
} 