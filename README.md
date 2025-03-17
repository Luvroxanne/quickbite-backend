# Vue 3 + TypeScript + Vite
# 在线餐饮管理系统

## 项目结构
tree
src/
├── assets/ # 静态资源文件
├── components/ # 组件目录
│ ├── common/ # 公共组件
│ │ └── NavBar.vue # 通用导航栏组件
│ ├── Home.vue # 首页组件
│ ├── Login.vue # 登录组件
│ ├── Register.vue # 注册组件
│ ├── MenuManage.vue # 菜单管理
│ ├── OrderManage.vue # 订单管理
│ ├── InventoryManage.vue # 库存管理
│ ├── DeliveryManage.vue # 配送管理
│ ├── CustomerReview.vue # 客户评价
│ ├── DataAnalysis.vue # 数据分析
│ └── Profile.vue # 个人信息
├── router/ # 路由配置
│ └── index.ts
├── stores/ # 状态管理
│ └── user.ts # 用户状态管理
├── App.vue # 根组件
└── main.ts # 入口文件


## 技术栈
- Vue 3
- TypeScript
- Element Plus
- Vite
- Pinia
- Vue Router

## API 接口设计

## API 接口文档

### 1. 用户认证模块
#### 1.1 登录接口
- 请求路径：`POST /api/auth/login`
- 功能说明：用户登录认证
- 请求参数：
  ```typescript
  {
    username: string    // 用户名
    password: string    // 密码（MD5加密）
  }
  ```
- 响应数据：
  ```typescript
  {
    token: string      // JWT认证令牌
    userInfo: {
      id: number       // 用户ID
      name: string     // 用户名称
      role: string     // 用户角色（merchant/customer）
    }
  }
  ```

#### 1.2 注册接口
- 请求路径：`POST /api/auth/register`
- 功能说明：新用户注册
- 请求参数：
  ```typescript
  {
    userType: 'merchant' | 'customer'  // 用户类型
    username: string    // 用户名
    password: string    // 密码（MD5加密）
    phone: string      // 手机号
    email: string      // 邮箱
    storeName?: string // 店铺名称（商家必填）
    address?: string   // 店铺地址（商家必填）
  }
  ```
- 响应数据：
  ```typescript
  {
    success: boolean   // 注册是否成功
    message: string    // 提示信息
  }
  ```

### 2. 菜单管理模块
#### 2.1 获取菜单列表
- 请求路径：`GET /api/menu/list`
- 功能说明：获取商家的菜品列表
- 查询参数：
  ```typescript
  {
    page?: number      // 页码（可选）
    pageSize?: number  // 每页数量（可选）
    category?: string  // 分类筛选（可选）
    status?: boolean   // 状态筛选（可选）
  }
  ```
- 响应数据：
  ```typescript
  {
    total: number      // 总记录数
    items: Array<{
      id: number       // 菜品ID
      name: string     // 菜品名称
      category: string // 分类
      price: number    // 价格
      status: boolean  // 上架状态
      image: string    // 图片URL
    }>
  }
  ```

#### 2.2 添加菜品
- 请求路径：`POST /api/menu/add`
- 功能说明：添加新的菜品
- 请求参数：
  ```typescript
  {
    name: string       // 菜品名称
    category: string   // 分类
    price: number      // 价格
    image: string      // 图片URL（通过文件上传接口获取）
    description?: string // 描述（可选）
    ingredients?: string[] // 配料（可选）
  }
  ```

#### 2.3 更新菜品
- 请求路径：`PUT /api/menu/:id`
- 功能说明：更新已有菜品信息
- 请求参数：
  ```typescript
  {
    name?: string      // 菜品名称（可选）
    category?: string  // 分类（可选）
    price?: number     // 价格（可选）
    status?: boolean   // 上架状态（可选）
    image?: string     // 图片URL（可选）
  }
  ```

### 3. 订单管理模块
#### 3.1 获取订单列表
- 请求路径：`GET /api/orders`
- 功能说明：获取商家的订单列表
- 查询参数：
  ```typescript
  {
    status?: string    // 订单状态筛选
    startDate?: string // 开始日期
    endDate?: string   // 结束日期
    page: number       // 页码
    pageSize: number   // 每页数量
  }
  ```
- 响应数据：
  ```typescript
  {
    total: number      // 总记录数
    items: Array<{
      id: string       // 订单ID
      customer: string // 客户信息
      items: string[]  // 订单商品
      total: number    // 订单金额
      status: string   // 订单状态
      time: string     // 下单时间
      paymentMethod: string // 支付方式
      remark?: string  // 备注
    }>
  }
  ```

### 4. 库存管理模块
#### 4.1 获取库存列表
- 请求路径：`GET /api/inventory`
- 功能说明：获取商家的库存信息
- 响应数据：
  ```typescript
  {
    items: Array<{
      id: number       // 库存ID
      name: string     // 原料名称
      category: string // 分类
      stock: number    // 库存数量
      unit: string     // 单位
      warning: number  // 预警值
      status: string   // 状态
      updateTime: string // 更新时间
      supplier?: string // 供应商
      price?: number   // 采购价格
    }>
  }
  ```

### 5. 配送管理模块
#### 5.1 获取配送订单
- 请求路径：`GET /api/delivery`
- 功能说明：获取待配送和配送中的订单
- 响应数据：
  ```typescript
  {
    orders: Array<{
      id: string       // 配送单号
      customer: {
        name: string   // 客户姓名
        address: string // 配送地址
        phone: string  // 联系电话
      }
      items: string[]  // 配送商品
      status: string   // 配送状态
      rider: string    // 配送员
      createTime: string // 创建时间
      estimatedTime: string // 预计送达时间
      distance?: number // 配送距离
      fee?: number     // 配送费
    }>
  }
  ```

### 6. 客户评价模块
#### 6.1 获取评价列表
- 请求路径：`GET /api/reviews`
- 功能说明：获取商家的客户评价
- 查询参数：
  ```typescript
  {
    status?: 'pending' | 'replied' // 评价状态
    page: number       // 页码
    pageSize: number   // 每页数量
    rating?: number    // 评分筛选
  }
  ```
- 响应数据：
  ```typescript
  {
    total: number      // 总记录数
    items: Array<{
      id: number       // 评价ID
      customer: string // 客户名称
      rating: number   // 评分
      content: string  // 评价内容
      orderInfo: string // 订单信息
      images: string[] // 图片列表
      createTime: string // 评价时间
      status: string   // 状态
      reply?: string   // 商家回复
    }>
  }
  ```

### 7. 数据分析模块
#### 7.1 获取销售统计
- 请求路径：`GET /api/statistics/sales`
- 功能说明：获取销售数据统计
- 查询参数：
  ```typescript
  {
    startDate: string  // 开始日期
    endDate: string    // 结束日期
    type?: 'day' | 'week' | 'month' // 统计类型
  }
  ```
- 响应数据：
  ```typescript
  {
    today: number      // 今日销售额
    week: number       // 本周销售额
    month: number      // 本月销售额
    yearOnYear: string // 同比增长
    monthOnMonth: string // 环比增长
    details: Array<{   // 详细数据
      date: string     // 日期
      amount: number   // 金额
      orders: number   // 订单数
    }>
  }
  ```

### 注意事项
1. 所有请求需要在 Header 中携带 token：
   ```typescript
   headers: {
     'Authorization': `Bearer ${token}`
   }
   ```
2. 文件上传有大小限制：
   - 图片：最大 5MB
   - 支持格式：jpg, png, webp
3. 接口返回格式统一为：
   ```typescript
   {
     code: number     // 状态码
     data: any        // 响应数据
     message: string  // 提示信息
   }
   ```
4. 错误处理：
   - 401：未登录或 token 失效
   - 403：权限不足
   - 404：资源不存在
   - 500：服务器错误

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

### 3. 菜品表 (dishes)
```sql
CREATE TABLE dishes (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    merchant_id BIGINT NOT NULL,
    name        VARCHAR(100) NOT NULL,
    category    VARCHAR(50) NOT NULL,
    price       DECIMAL(10,2) NOT NULL,
    image       VARCHAR(255),
    description TEXT,
    status      TINYINT DEFAULT 1,  -- 1: 上架, 0: 下架
    stock       INT DEFAULT 0,       -- 库存数量
    sales       INT DEFAULT 0,       -- 销量
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (merchant_id) REFERENCES merchants(id)
);
```

### 4. 订单表 (orders)
```sql
CREATE TABLE orders (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no        VARCHAR(50) NOT NULL UNIQUE,
    merchant_id     BIGINT NOT NULL,
    customer_id     BIGINT NOT NULL,
    total_amount    DECIMAL(10,2) NOT NULL,
    status          ENUM('pending', 'paid', 'preparing', 'delivering', 'completed', 'cancelled') NOT NULL,
    payment_method  VARCHAR(20),
    payment_status  TINYINT DEFAULT 0,  -- 0: 未支付, 1: 已支付
    remark          TEXT,
    address         VARCHAR(255) NOT NULL,
    contact_name    VARCHAR(50) NOT NULL,
    contact_phone   VARCHAR(20) NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (merchant_id) REFERENCES merchants(id),
    FOREIGN KEY (customer_id) REFERENCES users(id)
);
```

### 5. 订单详情表 (order_items)
```sql
CREATE TABLE order_items (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id    BIGINT NOT NULL,
    dish_id     BIGINT NOT NULL,
    quantity    INT NOT NULL,
    price       DECIMAL(10,2) NOT NULL,
    subtotal    DECIMAL(10,2) NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (dish_id) REFERENCES dishes(id)
);
```

### 6. 库存表 (inventory)
```sql
CREATE TABLE inventory (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    merchant_id BIGINT NOT NULL,
    name        VARCHAR(100) NOT NULL,
    category    VARCHAR(50) NOT NULL,
    stock       INT NOT NULL DEFAULT 0,
    unit        VARCHAR(20) NOT NULL,
    warning_level INT NOT NULL,
    status      ENUM('normal', 'warning', 'low') NOT NULL,
    supplier    VARCHAR(100),
    price       DECIMAL(10,2),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (merchant_id) REFERENCES merchants(id)
);
```

### 7. 配送表 (deliveries)
```sql
CREATE TABLE deliveries (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id        BIGINT NOT NULL,
    rider_id        BIGINT,
    status          ENUM('pending', 'assigned', 'delivering', 'completed') NOT NULL,
    estimated_time  VARCHAR(20),
    actual_time     TIMESTAMP,
    distance        DECIMAL(5,2),
    fee            DECIMAL(10,2),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (rider_id) REFERENCES users(id)
);
```

### 8. 评价表 (reviews)
```sql
CREATE TABLE reviews (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id    BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    merchant_id BIGINT NOT NULL,
    rating      TINYINT NOT NULL,
    content     TEXT,
    images      TEXT,  -- JSON array of image URLs
    status      ENUM('pending', 'replied') NOT NULL,
    reply       TEXT,
    reply_time  TIMESTAMP,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (customer_id) REFERENCES users(id),
    FOREIGN KEY (merchant_id) REFERENCES merchants(id)
);
```

### 9. 销售统计表 (sales_statistics)
```sql
CREATE TABLE sales_statistics (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    merchant_id     BIGINT NOT NULL,
    date            DATE NOT NULL,
    total_amount    DECIMAL(10,2) NOT NULL,
    order_count     INT NOT NULL,
    customer_count  INT NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (merchant_id) REFERENCES merchants(id)
);
```

### 数据库设计说明

1. 索引设计
- 所有外键字段都建立索引
- 经常查询的字段建立索引（如 order_no, status 等）
- 合理使用联合索引提高查询效率

2. 字段设计
- 使用 BIGINT 作为主键，预留足够空间
- 使用 DECIMAL 存储金额，保证精确度
- 使用 ENUM 限制状态字段的值范围
- 统一使用 UTF8MB4 字符集

3. 关联关系
- users -> merchants: 一对一
- merchants -> dishes: 一对多
- orders -> order_items: 一对多
- orders -> deliveries: 一对一
- orders -> reviews: 一对一

4. 注意事项
- 所有表都有创建和更新时间
- 关键数据添加状态字段
- 合理使用外键约束
- 重要字段添加唯一索引# quickbite-backend
