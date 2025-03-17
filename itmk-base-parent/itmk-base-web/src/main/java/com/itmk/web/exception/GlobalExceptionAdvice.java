package com.itmk.web.exception;

import com.itmk.exception.BusinessException;
import com.itmk.result.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: MK
 * @Description: 全局异常处理
 * @Date: 2024/3/20
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    /**
     * 处理 @RequestBody 参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultVo<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<String> errors = bindingResult.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        String errorMsg = String.join(", ", errors);
        log.error("参数校验异常：{}", errorMsg);
        return ResultVo.fail(errorMsg);
    }

    /**
     * 处理 @RequestParam 参数校验异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResultVo<?> handleConstraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String errorMsg = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        log.error("参数校验异常：{}", errorMsg);
        return ResultVo.fail(errorMsg);
    }

    /**
     * 处理 @ModelAttribute 参数校验异常
     */
    @ExceptionHandler(BindException.class)
    public ResultVo<?> handleBindException(BindException e) {
        List<String> errors = e.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        String errorMsg = String.join(", ", errors);
        log.error("参数校验异常：{}", errorMsg);
        return ResultVo.fail(errorMsg);
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResultVo<?> handleBusinessException(BusinessException e) {
        log.error("业务异常：{}", e.getMessage());
        return ResultVo.fail(e.getMessage());
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public ResultVo<?> handleException(Exception e) {
        log.error("系统异常：", e);
        return ResultVo.fail(e.getMessage());
    }
} 