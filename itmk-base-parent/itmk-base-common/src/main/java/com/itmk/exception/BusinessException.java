package com.itmk.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: MK
 * @Description: 自定义业务异常
 * @Date: 2024/3/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {
    /**
     * 错误码
     */
    private Integer code;
    /**
     * 错误信息
     */
    private String message;

    public BusinessException(String message) {
        this.code = 500;
        this.message = message;
    }

    public BusinessException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
} 