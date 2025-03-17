# 菜品管理 API 文档

## 基础信息
- 基础路径: `/api/menu`
- 需要认证: 需要在请求头携带 `Authorization: Bearer {token}`
- 权限要求: 商家角色

## 数据结构

### Dish - 菜品信息
typescript
{
id: number // 菜品ID
merchantId: number // 商家ID
name: string // 菜品名称
category: string // 分类
price: number // 价格
image: string // 图片URL
description: string // 描述
status: boolean // 状态(true:上架 false:下架)
stock: number // 库存
soldCount: number // 销量
createTime: string // 创建时间
updateTime: string // 更新时间
}

## API 接口

### 1. 获取菜品列表
GET /menu/list

请求参数：
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| category | string | 否 | 菜品分类 |
| status | boolean | 否 | 上架状态 |

响应示例：json
{
"code": 200,
"msg": "success",
"data": [
{
"id": 1,
"merchantId": 1001,
"name": "宫保鸡丁",
"category": "热菜",
"price": 28.00,
"image": "http://example.com/image.jpg",
"description": "香辣可口",
"status": true,
"stock": 100,
"soldCount": 0,
"createTime": "2024-03-20 10:00:00",
"updateTime": "2024-03-20 10:00:00"
}
]
}

### 2. 添加菜品
POST /api/menu/add

请求参数：
```json
{
  "name": "宫保鸡丁",
  "category": "热菜",
  "price": 28.00,
  "image": "http://example.com/image.jpg",
  "description": "香辣可口",
  "status": true,
  "stock": 100
}
```

参数说明：
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| name | string | 是 | 菜品名称 |
| category | string | 是 | 分类 |
| price | number | 是 | 价格(>0) |
| image | string | 否 | 图片URL |
| description | string | 否 | 描述 |
| status | boolean | 是 | 上架状态 |
| stock | number | 是 | 库存(>=0) |

响应示例：
```json
{
  "code": 200,
  "msg": "添加成功",
  "data": true
}
```

### 3. 更新菜品
PUT /api/menu/{id}

请求参数：同添加菜品

响应示例：
```json
{
  "code": 200,
  "msg": "更新成功",
  "data": true
}
```

### 4. 删除菜品
DELETE /api/menu/{id}

路径参数：
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | number | 是 | 菜品ID |

响应示例：
```json
{
  "code": 200,
  "msg": "删除成功",
  "data": true
}
```

### 5. 批量删除菜品
DELETE /api/menu/batch

请求参数：
```json
[1, 2, 3]  // 菜品ID数组
```

响应示例：
```json
{
  "code": 200,
  "msg": "批量删除成功",
  "data": true
}
```

### 6. 上传菜品图片
POST /api/upload/image

请求参数：
- Content-Type: multipart/form-data
- 参数名: file

响应示例：
```json
{
  "code": 200,
  "msg": "上传成功",
  "data": {
    "url": "http://example.com/image.jpg"
  }
}
```

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未认证 |
| 403 | 无权限 |
| 500 | 服务器错误 |

## 注意事项

1. 所有请求需要在 header 中携带 token
```http
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

2. 图片上传
- 支持格式: jpg, jpeg, png
- 大小限制: 2MB
- 返回的是图片URL，需要在添加/更新菜品时使用此URL

3. 权限控制
- 商家只能操作自己的菜品
- 添加菜品时会自动关联当前商家ID
- 更新和删除会校验菜品归属权

4. 字段验证
- name: 不能为空，长度1-50
- category: 不能为空
- price: 必须大于0
- stock: 必须大于等于0

## 调用示例

```typescript
// 获取菜品列表
const response = await request({
  url: '/api/menu/list',
  method: 'get',
  params: {
    category: '热菜',
    status: true
  }
})

// 添加菜品
const response = await request({
  url: '/api/menu/add',
  method: 'post',
  data: {
    name: '宫保鸡丁',
    category: '热菜',
    price: 28.00,
    image: 'http://example.com/image.jpg',
    description: '香辣可口',
    status: true,
    stock: 100
  }
})

// 上传图片
const formData = new FormData()
formData.append('file', file)
const response = await request({
  url: '/api/upload/image',
  method: 'post',
  data: formData,
  headers: {
    'Content-Type': 'multipart/form-data'
  }
})
```