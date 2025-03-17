# 开发指南

## 本地开发环境配置

### 1. 数据库初始化
```sql
-- 创建数据库
CREATE DATABASE restaurant_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 初始化管理员账号
INSERT INTO users (username, password, role, phone, email, status) 
VALUES ('admin', '$2a$10$XXXXXX', 'admin', '13800138000', 'admin@example.com', 1);

-- 初始化测试账号
INSERT INTO users (username, password, role, phone, email, status) 
VALUES 
('merchant_test', '$2a$10$XXXXXX', 'merchant', '13800138001', 'merchant@example.com', 1),
('customer_test', '$2a$10$XXXXXX', 'customer', '13800138002', 'customer@example.com', 1);

-- 初始化测试数据
INSERT INTO merchants (user_id, store_name, address, description, business_hours, status) 
VALUES (2, '测试餐厅', '测试地址', '这是一家测试餐厅', '09:00-22:00', 1);
```

### 2. 开发环境配置
```yaml
# application-dev.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/restaurant_management?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=false
    username: root
    password: Crt13678230554
  
  redis:
    host: localhost
    port: 6379
    
jwt:
  secret: your-secret-key
  expiration: 604800000  # 7天
```

### 3. 测试账号
- 管理员：admin / 123456
- 商家：merchant_test / 123456
- 客户：customer_test / 123456

### 4. 开发环境检查项
- MySQL (端口3306)
- Redis (端口6379)
- 上传文件目录权限
- IDE配置 (JDK, Maven, Lombok)

### 5. 注意事项
- 使用测试数据开发
- 提交前格式化代码
- 添加必要注释
- 本地调试用log
```

### 参考文档
- [API文档](../README.md#api-接口文档)
- [数据库设计](../README.md#数据库设计) 

## API 接口设计

## API 接口文档

## 基础说明

- 基础URL: `http://localhost:8080`
- 请求头: 除登录和注册外，所有接口需要在请求头中携带 `Authorization: Bearer {token}`
- 响应格式:
```json
{
    "code": 200,       // 状态码：200成功，500失败
    "msg": "success",  // 提示信息
    "data": {}         // 响应数据
}
```

## 统一认证接口

### 1. 登录
- 请求路径: `/api/auth/login`
- 请求方式: POST
- 请求参数:
```json
{
    "username": "string",  // 用户名
    "password": "string"   // 密码
}
```
- 响应数据:
```json
{
    "token": "string",     // JWT令牌
    "user": {
        "id": "number",    // 用户ID
        "username": "string", // 用户名
        "role": "string",  // 角色:merchant/customer
        "phone": "string", // 手机号
        "email": "string", // 邮箱
        "avatar": "string" // 头像
    }
}
```

### 2. 注册
- 请求路径: `/api/auth/register`
- 请求方式: POST
- 请求参数:
```json
{
    "userType": "string",    // 用户类型:merchant/customer
    "username": "string",    // 用户名
    "password": "string",    // 密码
    "phone": "string",      // 手机号
    "email": "string",      // 邮箱
    "storeName": "string",  // 店铺名称(商家必填)
    "address": "string"     // 店铺地址(商家必填)
}
```

## 商家接口

### 1. 获取商家统计信息
- 请求路径: `/api/merchant/stats`
- 请求方式: GET
- 响应数据:
```json
{
    "todayOrders": "number",      // 今日订单数
    "todayRevenue": "number",     // 今日收入
    "yesterdayOrders": "number",  // 昨日订单数
    "yesterdayRevenue": "number", // 昨日收入
    "totalCustomers": "number",   // 总顾客数
    "avgRating": "number",        // 平均评分
    "pendingOrders": "number",    // 待处理订单数
    "totalSales": "number"        // 总销售额
}
```

### 2. 更新商家信息
- 请求路径: `/api/merchant/profile`
- 请求方式: PUT
- 请求参数:
```json
{
    "storeName": "string",      // 店铺名称
    "address": "string",        // 店铺地址
    "logo": "string",          // 店铺logo
    "description": "string",   // 店铺描述
    "businessHours": "string", // 营业时间
    "status": "number"        // 状态:1营业中,0休息中
}
```

### 3. 修改密码
- 请求路径: `/api/merchant/password/update`
- 请求方式: PUT
- 请求参数:
```json
{
    "oldPassword": "string",  // 原密码
    "newPassword": "string"   // 新密码
}
```

### 4. 获取通知列表
- 请求路径: `/api/merchant/notifications`
- 请求方式: GET
- 请求参数:
  - page: 页码(默认1)
  - pageSize: 每页数量(默认10)
- 响应数据:
```json
{
    "total": "number",      // 总记录数
    "records": [{
        "id": "number",     // 通知ID
        "title": "string",  // 标题
        "content": "string", // 内容
        "type": "string",   // 类型
        "isRead": "number", // 是否已读:1已读,0未读
        "createdAt": "string" // 创建时间
    }]
}
```

### 5. 标记通知已读
- 请求路径: `/api/merchant/notifications/{id}/read`
- 请求方式: PUT

### 6. 获取未读通知数量
- 请求路径: `/api/merchant/notifications/unread/count`
- 请求方式: GET

## 菜品管理接口

### 1. 获取菜品列表
- 请求路径: `/api/menu`
- 请求方式: GET
- 请求参数:
  - page: 页码(默认1)
  - pageSize: 每页数量(默认10)
  - category: 分类(可选)
  - status: 状态(可选)

### 2. 添加菜品
- 请求路径: `/api/menu/add`
- 请求方式: POST
- 请求参数:
```json
{
    "name": "string",       // 菜品名称
    "price": "number",      // 价格
    "category": "string",   // 分类
    "image": "string",      // 图片
    "description": "string", // 描述
    "status": "number",     // 状态:1上架,0下架
    "stock": "number"       // 库存
}
```

### 3. 更新菜品
- 请求路径: `/api/menu/{id}`
- 请求方式: PUT
- 请求参数: 同添加菜品

## 订单管理接口

### 1. 获取订单列表
- 请求路径: `/api/orders`
- 请求方式: GET
- 请求参数:
  - page: 页码(默认1)
  - pageSize: 每页数量(默认10)
  - status: 订单状态(可选)

### 2. 订单评价
- 请求路径: `/api/orders/{id}/rate`
- 请求方式: POST
- 请求参数:
```json
{
    "rating": "number",    // 评分(1-5)
    "comment": "string"    // 评价内容
}
```

### 3. 申请退款
- 请求路径: `/api/orders/{id}/refund`
- 请求方式: POST
- 请求参数:
```json
{
    "reason": "string"    // 退款原因
}
```

## 错误码说明

- 200: 成功
- 401: 未登录或token已过期
- 403: 无权限访问
- 404: 资源不存在
- 500: 服务器内部错误

## 注意事项

1. 所有需要鉴权的接口必须在请求头中携带token
2. 商家相关接口需要商家角色权限
3. 客户相关接口需要客户角色权限
4. 文件上传接口的Content-Type需要设置为multipart/form-data

   ## 数据库设计

### 1. 用户表 (users)
```sql
CREATE TABLE users (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    username    VARCHAR(50) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    role        ENUM('merchant', 'customer', 'admin') NOT NULL,
    phone       VARCHAR(20),
    email       VARCHAR(100),
    avatar      VARCHAR(255),
    status      TINYINT DEFAULT 1,  -- 1: 正常, 0: 禁用
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 2. 商家信息表 (merchants)
```sql
CREATE TABLE merchants (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id     BIGINT NOT NULL,
    store_name  VARCHAR(100) NOT NULL,
    address     VARCHAR(255) NOT NULL,
    logo        VARCHAR(255),
    description TEXT,
    business_hours VARCHAR(100),
    rating      DECIMAL(2,1) DEFAULT 5.0,
    status      TINYINT DEFAULT 1,  -- 1: 营业中, 0: 休息中
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```