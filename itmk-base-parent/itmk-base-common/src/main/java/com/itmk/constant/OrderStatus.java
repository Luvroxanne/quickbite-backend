package com.itmk.constant;

import lombok.Getter;

/**
 * @Author: MK
 * @Description: 订单状态枚举
 * @Date: 2024/3/20
 */
@Getter
public enum OrderStatus {
    PENDING("pending", "待支付"),
    PAID("paid", "已支付"),
    PREPARING("preparing", "制作中"),
    DELIVERING("delivering", "配送中"),
    COMPLETED("completed", "已完成"),
    CANCELLED("cancelled", "已取消");

    private final String code;
    private final String desc;

    OrderStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static OrderStatus getByCode(String code) {
        for (OrderStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    // 提供静态方法来获取状态码
    public static String getCode(OrderStatus status) {
        return status.getCode();
    }

    // 提供静态方法来获取描述
    public static String getDesc(OrderStatus status) {
        return status.getDesc();
    }
} 