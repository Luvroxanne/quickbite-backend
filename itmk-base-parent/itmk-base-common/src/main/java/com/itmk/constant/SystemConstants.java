package com.itmk.constant;

/**
 * @Author: MK
 * @Description: 系统常量
 * @Date: 2024/3/20
 */
public class SystemConstants {
    /**
     * Token前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 用户ID的Key
     */
    public static final String USER_ID_KEY = "userId";

    /**
     * 用户角色的Key
     */
    public static final String USER_ROLE_KEY = "role";

    /**
     * 商家角色
     */
    public static final String ROLE_MERCHANT = "merchant";

    /**
     * 客户角色
     */
    public static final String ROLE_CUSTOMER = "customer";

    /**
     * 管理员角色
     */
    public static final String ROLE_ADMIN = "admin";

    /**
     * 骑手角色
     */
    public static final String ROLE_RIDER = "rider";

    /**
     * 支付方式
     */
    public static final String PAYMENT_WECHAT = "WECHAT";
    public static final String PAYMENT_ALIPAY = "ALIPAY";

    /**
     * 支付状态
     */
    public static final int PAYMENT_STATUS_UNPAID = 0;
    public static final int PAYMENT_STATUS_PAID = 1;

    private SystemConstants() {
        // 私有构造函数，防止实例化
    }
} 