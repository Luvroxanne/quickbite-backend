-- 创建数据库
CREATE DATABASE IF NOT EXISTS restaurant_management
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE restaurant_management;

-- 创建用户表
CREATE TABLE users (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username    VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password    VARCHAR(255) NOT NULL COMMENT '密码',
    role        ENUM('merchant', 'customer', 'admin', 'rider') NOT NULL COMMENT '角色';
    phone       VARCHAR(20) COMMENT '手机号',
    email       VARCHAR(100) COMMENT '邮箱',
    avatar      VARCHAR(255) COMMENT '头像',
    status      TINYINT DEFAULT 1 COMMENT '状态：1正常，0禁用',
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username),
    INDEX idx_phone (phone),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 创建商家信息表
CREATE TABLE merchants (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '商家ID',
    user_id     BIGINT NOT NULL COMMENT '关联用户ID',
    store_name  VARCHAR(100) NOT NULL COMMENT '店铺名称',
    address     VARCHAR(255) NOT NULL COMMENT '店铺地址',
    logo        VARCHAR(255) COMMENT '店铺logo',
    description TEXT COMMENT '店铺描述',
    business_hours VARCHAR(100) COMMENT '营业时间',
    rating      DECIMAL(2,1) DEFAULT 5.0 COMMENT '店铺评分',
    status      TINYINT DEFAULT 1 COMMENT '状态：1营业中，0休息中',
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_user_id (user_id),
    INDEX idx_store_name (store_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商家信息表';

-- 创建菜品表
CREATE TABLE dishes (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '菜品ID',
    merchant_id BIGINT NOT NULL COMMENT '商家ID',
    name        VARCHAR(100) NOT NULL COMMENT '菜品名称',
    category    VARCHAR(50) NOT NULL COMMENT '菜品分类',
    price       DECIMAL(10,2) NOT NULL COMMENT '价格',
    image       VARCHAR(255) COMMENT '图片',
    description TEXT COMMENT '描述',
    status      TINYINT DEFAULT 1 COMMENT '状态：1上架，0下架',
    stock       INT DEFAULT 0 COMMENT '库存数量',
    sales       INT DEFAULT 0 COMMENT '销量',
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标记：0正常，1已删除',
    FOREIGN KEY (merchant_id) REFERENCES merchants(id),
    INDEX idx_merchant_id (merchant_id),
    INDEX idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品表';

-- 创建订单表
CREATE TABLE orders (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    order_no        VARCHAR(50) NOT NULL UNIQUE COMMENT '订单编号',
    merchant_id     BIGINT NOT NULL COMMENT '商家ID',
    customer_id     BIGINT NOT NULL COMMENT '顾客ID',
    total_amount    DECIMAL(10,2) NOT NULL COMMENT '订单总额',
    status          ENUM('pending', 'paid', 'preparing', 'delivering', 'completed', 'cancelled') NOT NULL COMMENT '订单状态',
    payment_method  VARCHAR(20) COMMENT '支付方式',
    payment_status  TINYINT DEFAULT 0 COMMENT '支付状态：0未支付，1已支付',
    remark          TEXT COMMENT '备注',
    address         VARCHAR(255) NOT NULL COMMENT '配送地址',
    contact_name    VARCHAR(50) NOT NULL COMMENT '联系人',
    contact_phone   VARCHAR(20) NOT NULL COMMENT '联系电话',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    refund_status TINYINT DEFAULT 0 COMMENT '退款状态：0未退款，1已退款',
    refund_time     TIMESTAMP COMMENT '退款时间',
    refund_reason   TEXT COMMENT '退款原因',
    FOREIGN KEY (merchant_id) REFERENCES merchants(id),
    FOREIGN KEY (customer_id) REFERENCES users(id),
    INDEX idx_order_no (order_no),
    INDEX idx_merchant_id (merchant_id),
    INDEX idx_customer_id (customer_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 创建订单详情表
CREATE TABLE order_items (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单项ID',
    order_id    BIGINT NOT NULL COMMENT '订单ID',
    dish_id     BIGINT NOT NULL COMMENT '菜品ID',
    quantity    INT NOT NULL COMMENT '数量',
    price       DECIMAL(10,2) NOT NULL COMMENT '单价',
    subtotal    DECIMAL(10,2) NOT NULL COMMENT '小计',
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (dish_id) REFERENCES dishes(id),
    INDEX idx_order_id (order_id),
    INDEX idx_dish_id (dish_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单详情表';

-- 创建库存表
CREATE TABLE inventory (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '库存ID',
    merchant_id BIGINT NOT NULL COMMENT '商家ID',
    name        VARCHAR(100) NOT NULL COMMENT '原料名称',
    category    VARCHAR(50) NOT NULL COMMENT '分类',
    stock       INT NOT NULL DEFAULT 0 COMMENT '库存量',
    unit        VARCHAR(20) NOT NULL COMMENT '单位',
    warning_level INT NOT NULL COMMENT '警戒库存',
    status      ENUM('normal', 'warning', 'low') NOT NULL COMMENT '状态',
    supplier    VARCHAR(100) COMMENT '供应商',
    price       DECIMAL(10,2) COMMENT '采购价',
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (merchant_id) REFERENCES merchants(id),
    INDEX idx_merchant_id (merchant_id),
    INDEX idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存表';

-- 创建配送表
CREATE TABLE deliveries (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '配送ID',
    order_id        BIGINT NOT NULL COMMENT '订单ID',
    rider_id        BIGINT COMMENT '骑手ID',
    merchant_id     BIGINT NOT NULL COMMENT '商家ID',
    status          ENUM('pending', 'assigned', 'delivering', 'completed') NOT NULL COMMENT '配送状态',
    estimated_time  VARCHAR(20) COMMENT '预计送达时间',
    actual_time     TIMESTAMP COMMENT '实际送达时间',
    distance        DECIMAL(5,2) COMMENT '配送距离',
    fee            DECIMAL(10,2) COMMENT '配送费',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (rider_id) REFERENCES users(id),
    FOREIGN KEY (merchant_id) REFERENCES merchants(id),
    INDEX idx_order_id (order_id),
    INDEX idx_rider_id (rider_id),
    INDEX idx_merchant_id (merchant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='配送表';

-- 创建评价表
CREATE TABLE reviews (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '评价ID',
    order_id    BIGINT NOT NULL COMMENT '订单ID',
    customer_id BIGINT NOT NULL COMMENT '顾客ID',
    merchant_id BIGINT NOT NULL COMMENT '商家ID',
    rating      TINYINT NOT NULL COMMENT '评分',
    content     TEXT COMMENT '评价内容',
    images      TEXT COMMENT '图片JSON数组',
    status      ENUM('pending', 'replied') NOT NULL COMMENT '状态',
    reply       TEXT COMMENT '商家回复',
    reply_time  TIMESTAMP COMMENT '回复时间',
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (customer_id) REFERENCES users(id),
    FOREIGN KEY (merchant_id) REFERENCES merchants(id),
    INDEX idx_order_id (order_id),
    INDEX idx_customer_id (customer_id),
    INDEX idx_merchant_id (merchant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评价表';

-- 创建销售统计表
CREATE TABLE sales_statistics (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '统计ID',
    merchant_id     BIGINT NOT NULL COMMENT '商家ID',
    date            DATE NOT NULL COMMENT '统计日期',
    total_amount    DECIMAL(10,2) NOT NULL COMMENT '总金额',
    order_count     INT NOT NULL COMMENT '订单数',
    customer_count  INT NOT NULL COMMENT '顾客数',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (merchant_id) REFERENCES merchants(id),
    INDEX idx_merchant_id (merchant_id),
    INDEX idx_date (date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售统计表';

-- 创建通知表
CREATE TABLE notifications (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '通知ID',
    user_id     BIGINT NOT NULL COMMENT '用户ID',
    title       VARCHAR(100) NOT NULL COMMENT '通知标题',
    content     TEXT NOT NULL COMMENT '通知内容',
    type        VARCHAR(50) NOT NULL COMMENT '通知类型',
    is_read     TINYINT DEFAULT 0 COMMENT '是否已读(0:未读 1:已读)',
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知表';
