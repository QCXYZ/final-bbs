# 接口文档

### 1. 认证与授权

#### 1.1 认证

- **接口描述**：用户登录认证，获取访问令牌。

- **请求URL**：`/api/auth/login`

- **请求方式**：`POST`

- **请求参数**：

  | 参数名   | 必选 | 类型   | 说明   |
        | -------- | ---- | ------ | ------ |
  | username | 是   | string | 用户名 |
  | password | 是   | string | 密码   |

- **返回示例**：

  ```json
  {
    "code": 200,
    "message": "登录成功",
    "data": {
      "token": "用户令牌"
    }
  }
  ```

#### 1.2 授权

- **接口描述**：验证用户的操作权限。

- **请求URL**：`/api/auth/authorize`

- **请求方式**：`POST`

- **请求参数**：

  | 参数名 | 必选 | 类型   | 说明       |
        | ------ | ---- | ------ | ---------- |
  | token  | 是   | string | 用户令牌   |
  | action | 是   | string | 请求的操作 |

- **返回示例**：

  ```json
  {
    "code": 200,
    "message": "授权成功",
    "data": {
      "authorized": true
    }
  }
  ```

### 2. 用户管理

#### 2.1 注册与登录

- **接口描述**：用户注册并登录，支持QQ登录。

- **请求URL**：`/api/auth/register`

- **请求方式**：`POST`

- **请求参数**：

  | 参数名   | 必选 | 类型   | 说明                       |
        | -------- | ---- | ------ | -------------------------- |
  | username | 是   | string | 用户名                     |
  | password | 是   | string | 密码                       |
  | qqId     | 否   | string | QQ号码（可选，用于QQ登录） |

- **返回示例**：

  ```json
  {
    "code": 200,
    "message": "注册并登录成功",
    "data": {
      "token": "用户令牌"
    }
  }
  ```

#### 2.2 个人资料管理

- **接口描述**：允许用户编辑个人资料。

- **请求URL**：`/api/user/profile`

- **请求方式**：`PUT`

- **请求参数**：

  | 参数名       | 必选 | 类型   | 说明     |
        | ------------ | ---- | ------ | -------- |
  | nickname     | 否   | string | 昵称     |
  | avatar       | 否   | string | 头像URL  |
  | personalSign | 否   | string | 个性签名 |

- **返回示例**：

  ```json
  {
    "code": 200,
    "message": "个人资料更新成功",
    "data": null
  }
  ```

#### 2.3 权限与角色管理

- **接口描述**：定义不同的用户角色和权限。

- **请求URL**：`/api/user/role`

- **请求方式**：`GET`

- **请求参数**：无

- **返回示例**：

  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      {
        "role": "普通用户",
        "permissions": ["浏览", "评论", "点赞"]
      },
      {
        "role": "版主",
        "permissions": ["浏览", "评论", "点赞", "帖子管理"]
      },
      {
        "role": "管理员",
        "permissions": ["全部权限"]
      }
    ]
  }
  ```

### 3. 内容管理

### 3.1 帖子发布与编辑

- **接口说明**：用户发布和编辑帖子。

- **请求方法**：POST / PUT

- **请求URL**：`/content/post` (发布) / `/content/post/{postId}` (编辑)

- **请求参数**：

  | 参数名   | 必选 | 类型   | 说明     |
        | -------- | ---- | ------ | -------- |
  | userId   | 是   | Long   | 用户ID   |
  | title    | 是   | string | 帖子标题 |
  | content  | 是   | string | 帖子内容 |
  | tags     | 否   | array  | 标签     |
  | category | 否   | string | 分类     |

- **返回示例**（发布）：

```json
{
  "code": 200,
  "message": "帖子发布成功",
  "data": {
    "postId": 123456
  }
}
```

- **返回示例**（编辑）：

```json
{
  "code": 200,
  "message": "帖子编辑成功",
  "data": null
}
```

### 3.2 分类与标签

- **接口说明**：管理帖子的分类和标签。

- **请求方法**：GET / POST / PUT / DELETE

- **请求URL**：`/content/categories` (获取/添加分类) / `/content/tags` (获取/添加标签)

- **请求参数**：

  | 参数名 | 必选 | 类型   | 说明           |
        | ------ | ---- | ------ | -------------- |
  | name   | 是   | string | 分类或标签名称 |

- **返回示例**（获取分类）：

```json
{
  "code": 200,
  "message": "分类获取成功",
  "data": [
    {
      "categoryId": 1,
      "name": "科技"
    },
    {
      "categoryId": 2,
      "name": "生活"
    }
  ]
}
```

- **返回示例**（添加分类）：

```json
{
  "code": 200,
  "message": "分类添加成功",
  "data": {
    "categoryId": 3,
    "name": "娱乐"
  }
}
```

### 3.3 评论与回复

- **接口说明**：对帖子的评论以及对评论的回复。

- **请求方法**：POST / PUT / DELETE

- **请求URL**：`/content/comment` (发布评论) / `/content/comment/{commentId}` (编辑或删除评论)

- **请求参数**：

  | 参数名   | 必选 | 类型   | 说明                   |
        | -------- | ---- | ------ | ---------------------- |
  | userId   | 是   | long   | 用户ID                 |
  | postId   | 是   | long   | 帖子ID                 |
  | content  | 是   | string | 评论内容               |
  | parentId | 否   | long   | 父评论ID（回复时使用） |

- **返回示例**（发布评论）：

```json
{
  "code": 200,
  "message": "评论发布成功",
  "data": {
    "commentId": 78910
  }
}
```

- **返回示例**（编辑评论）：

```json
{
  "code": 200,
  "message": "评论编辑成功",
  "data": null
}
```

### 3.4 内容审核

- **接口说明**：自动或人工审核帖子和评论内容。

- **请求方法**：GET / POST

- **请求URL**：`/content/audit`

- **请求参数**：

  | 参数名    | 必选 | 类型   | 说明                            |
        | --------- | ---- | ------ | ------------------------------- |
  | contentId | 是   | long   | 内容ID（帖子或评论ID）          |
  | action    | 是   | string | 审核操作（"approve"或"reject"） |

- **返回示例**：

```json
{
  "code": 200,
  "message": "内容审核完成",
  "data": {
    "contentId": 123456,
    "status": "approved"
    // 或 "rejected"
  }
}
```

## 4. 交互功能

### 4.1 点赞与收藏

- **接口说明**：用户可以对帖子或评论进行点赞和收藏。

- **请求方法**：POST / DELETE

- **请求URL**：`/interaction/like` (点赞) / `/interaction/favorite` (收藏)

- **请求参数**：

  | 参数名    | 必选 | 类型 | 说明                   |
        | --------- | ---- | ---- | ---------------------- |
  | userId    | 是   | long | 用户ID                 |
  | contentId | 是   | long | 内容ID（帖子或评论ID） |

- **返回示例**（点赞）：

```json
{
  "code": 200,
  "message": "点赞成功",
  "data": null
}
```

- **返回示例**（收藏）：

```json
{
  "code": 200,
  "message": "收藏成功",
  "data": null
}
```

### 4.2 消息通知

- **接口说明**：用户接收实时消息通知，如帖子评论、点赞、私信等。

- **请求方法**：GET

- **请求URL**：`/interaction/notifications`

- **请求参数**：

  | 参数名 | 必选 | 类型 | 说明   |
        | ------ | ---- | ---- | ------ |
  | userId | 是   | long | 用户ID |

- **返回示例**：

```json
{
  "code": 200,
  "message": "通知获取成功",
  "data": [
    {
      "notificationId": 1001,
      "type": "comment",
      "message": "您的帖子有了新评论"
    },
    {
      "notificationId": 1002,
      "type": "like",
      "message": "您的评论收到了新的点赞"
    }
  ]
}
```

### 4.3 搜索功能

- **接口说明**：根据关键词、分类等条件进行内容搜索。

- **请求方法**：GET

- **请求URL**：`/interaction/search`

- **请求参数**：

  | 参数名   | 必选 | 类型   | 说明         |
        | -------- | ---- | ------ | ------------ |
  | keyword  | 否   | string | 搜索关键词   |
  | category | 否   | string | 分类         |
  | tag      | 否   | string | 标签         |
  | page     | 否   | int    | 页码         |
  | limit    | 否   | int    | 每页显示数量 |

- **返回示例**：

```json
{
  "code": 200,
  "message": "搜索成功",
  "data": {
    "posts": [
      {
        "postId": 123456,
        "title": "搜索到的帖子标题"
      }
    ],
    "pagination": {
      "currentPage": 1,
      "totalPages": 5
    }
  }
}
```

## 5. 数据分析

### 5.1 用户行为分析

- **接口说明**：追踪用户行为数据。

- **请求方法**：GET

- **请求URL**：`/analytics/user-behavior`

- **请求参数**：

  | 参数名 | 必选 | 类型 | 说明   |
        | ------ | ---- | ---- | ------ |
  | userId | 否   | long | 用户ID |

- **返回示例**：

```json
{
  "code": 200,
  "message": "用户行为数据获取成功",
  "data": {
    "visitCount": 12345,
    "activeDegree": "high",
    "preferenceTags": ["科技", "游戏"]
  }
}
```

### 5.2 内容分析

- **接口说明**：分析内容数据。
- **请求方法**：GET
- **请求URL**：`/analytics/content-analysis`
- **请求参数**：无

- **返回示例**：

```json
{
  "code": 200,
  "message": "内容分析数据获取成功",
  "data": {
    "popularPosts": [
      {
        "postId": 123456,
        "title": "热门帖子标题",
        "views": 98765
      }
    ],
    "trendingTags": [
      "健康",
      "旅行"
    ]
  }
}
```

## 6. 系统维护

### 6.1 备份与恢复

- **接口说明**：对数据进行备份与恢复。

- **请求方法**：POST / GET

- **请求URL**：`/system/backup` (备份) / `/system/restore` (恢复)

- **请求参数**：

  | 参数名   | 必选 | 类型 | 说明               |
        | -------- | ---- | ---- | ------------------ |
  | backupId | 否   | long | 备份ID，恢复时使用 |

- **返回示例**（备份）：

```json
{
  "code": 200,
  "message": "数据备份成功",
  "data": {
    "backupId": 20240414
  }
}
```

- **返回示例**（恢复）：

```json
{
  "code": 200,
  "message": "数据恢复成功",
  "data": null
}
```

### 6.2 性能监控

- **接口说明**：监控网站性能。
- **请求方法**：GET
- **请求URL**：`/system/performance`
- **请求参数**：无

- **返回示例**：

```json
{
  "code": 200,
  "message": "性能数据获取成功",
  "data": {
    "responseTime": "100ms",
    "serverLoad": "75%"
  }
}
```

### 6.3 日志管理

- **接口说明**：记录和管理系统日志。

- **请求方法**：GET

- **请求URL**：`/system/logs`

- **请求参数**：

  | 参数名 | 必选 | 类型   | 说明                         |
        | ------ | ---- | ------ | ---------------------------- |
  | type   | 否   | string | 日志类型（"user"或"system"） |
  | page   | 否   | int    | 页码                         |
  | limit  | 否   | int    | 每页显示数量                 |

- **返回示例**：

```json
{
  "code": 200,
  "message": "日志获取成功",
  "data": {
    "logs": [
      {
        "logId": 001,
        "type": "user",
        "message": "用户testUser登录成功"
      },
      {
        "logId": 002,
        "type": "system",
        "message": "服务器CPU使用率达到90%"
      }
    ],
    "pagination": {
      "currentPage": 1,
      "totalPages": 3
    }
  }
}
```

以上接口文档提供了各个功能模块的基本接口定义和示例返回数据。实际应用中，接口可能还会包含更多的细节和安全措施，如参数校验等。
