# 接口文档

### 用户资料模块

1. **查看个人资料接口**

    - **请求方式：** GET

    - **URL：** /api/user/profile

    - **请求示例：**

      ```http
      GET /api/user/profile HTTP/1.1
      Host: example.com
      Authorization: Bearer {token}
      ```

    - **响应示例：**

      ```json
      {
        "id": 1,
        "username": "user123",
        "email": "user@example.com",
        "avatar": "https://example.com/avatar.jpg",
        "nickname": "User",
        "signature": "Hello, world!"
      }
      ```

2. **资料编辑接口**

    - **请求方式：** PUT

    - **URL：** /api/user/profile

    - **请求示例：**

      ```http
      PUT /api/user/profile HTTP/1.1
      Host: example.com
      Content-Type: application/json
      Authorization: Bearer {token}
      
      {
        "avatar": "https://example.com/new-avatar.jpg",
        "nickname": "New Nickname",
        "signature": "New Signature"
      }
      ```

    - **响应示例：**

      ```json
      {
        "message": "Profile updated successfully"
      }
      ```

### 帖子管理模块

1. **帖子发布接口**

    - **请求方式：** POST

    - **URL：** /api/posts

    - **请求示例：**

      ```http
      POST /api/posts HTTP/1.1
      Host: example.com
      Content-Type: application/json
      Authorization: Bearer {token}
      
      {
        "title": "New Post",
        "content": "This is the content of the new post",
        "images": ["https://example.com/image1.jpg", "https://example.com/image2.jpg"],
        "videos": ["https://example.com/video1.mp4"]
      }
      ```

    - **响应示例：**

      ```json
      {
        "message": "Post created successfully"
      }
      ```

2. **获取所有帖子接口**

    - **请求方式：** GET

    - **URL：** /api/posts

    - **请求示例：**

      ```http
      GET /api/posts?page=1&limit=10 HTTP/1.1
      Host: example.com
      ```

    - **响应示例：**

      ```json
      {
        "total": 20,
        "posts": [
          {
            "id": 1,
            "title": "Post 1",
            "content": "Content of post 1",
            "images": ["https://example.com/image1.jpg"],
            "videos": [],
            "user": {
              "id": 1,
              "username": "user123"
            },
            "createdAt": "2024-04-16T12:00:00",
            "updatedAt": "2024-04-16T12:00:00"
          }
        ]
      }
      ```

3. **查看帖子详情接口**

    - **请求方式：** GET

    - **URL：** /api/posts/{postId}

    - **请求示例：**

      ```http
      GET /api/posts/1 HTTP/1.1
      Host: example.com
      ```

    - **响应示例：**

      ```json
      {
        "id": 1,
        "title": "Post 1",
        "content": "Content of post 1",
        "images": ["https://example.com/image1.jpg"],
        "videos": [],
        "user": {
          "id": 1,
          "username": "user123"
        },
        "createdAt": "2024-04-16T12:00:00",
        "updatedAt": "2024-04-16T12:00:00"
      }
      ```

4. **帖子编辑接口**

    - **请求方式：** PUT

    - **URL：** /api/posts/{postId}

    - **请求示例：**

      ```http
      PUT /api/posts/1 HTTP/1.1
      Host: example.com
      Content-Type: application/json
      Authorization: Bearer {token}
      
      {
        "title": "Updated Post Title",
        "content": "Updated content of the post"
      }
      ```

    - **响应示例：**

      ```json
      {
        "message": "Post updated successfully"
      }
      ```

5. **帖子删除接口**

    - **请求方式：** DELETE

    - **URL：** /api/posts/{postId}

    - **请求示例：**

      ```http
      DELETE /api/posts/1 HTTP/1.1
      Host: example.com
      Authorization: Bearer {token}
      ```

    - **响应示例：**

      ```json
      {
        "message": "Post deleted successfully"
      }
      ```

6. **图片视频上传接口**

    - **请求方式：** POST

    - **URL：** /api/posts/{postId}/media

    - **请求示例：**

      ```http
      POST /api/posts/1/media HTTP/1.1
      Host: example.com
      Content-Type: multipart/form-data
      Authorization: Bearer {token}
      
      --boundary
      Content-Disposition: form-data; name="media"; filename="image.jpg"
      Content-Type: image/jpeg
      
      {binary data}
      --boundary
      Content-Disposition: form-data; name="media"; filename="video.mp4"
      Content-Type: video/mp4
      
      {binary data}
      --boundary--
      ```

    - **响应示例：**

      ```json
      {
        "message": "Media uploaded successfully"
      }
      ```

7. **帖子审核开关接口**

    - **请求方式：** PUT

    - **URL：** /api/posts/review/switch

    - **请求示例：**

      ```http
      PUT /api/posts/review/switch HTTP/1.1
      Host: example.com
      Content-Type: application/json
      Authorization: Bearer {adminToken}
      
      {
        "enabled": true
      }
      ```

    - **响应示例：**

      ```json
      {
        "message": "Post review switch updated successfully"
      }
      ```

8. **帖子审核接口**

    - **请求方式：** PUT

    - **URL：** /api/posts/{postId}/review

    - **请求示例：**

      ```http
      PUT /api/posts/1/review HTTP/1.1
      Host: example.com
      Content-Type: application/json
      Authorization: Bearer {adminToken
      ```

}

     {
       "approved": true
     }
     ```

- **响应示例：**

  ```json
  {
    "message": "Post reviewed successfully"
  }
  ```

### 互动交流模块

1. **发表评论接口**

    - **请求方式：** POST

    - **URL：** /api/posts/{postId}/comments

    - **请求示例：**

      ```http
      POST /api/posts/1/comments HTTP/1.1
      Host: example.com
      Content-Type: application/json
      Authorization: Bearer {token}
      
      {
        "content": "This is a comment"
      }
      ```

    - **响应示例：**

      ```json
      {
        "message": "Comment posted successfully"
      }
      ```

2. **删除评论接口**

    - **请求方式：** DELETE

    - **URL：** /api/posts/{postId}/comments/{commentId}

    - **请求示例：**

      ```http
      DELETE /api/posts/1/comments/1 HTTP/1.1
      Host: example.com
      Authorization: Bearer {token}
      ```

    - **响应示例：**

      ```json
      {
        "message": "Comment deleted successfully"
      }
      ```

3. **点赞或评分接口**

    - **请求方式：** POST

    - **URL：** /api/posts/{postId}/like

    - **请求示例：**

      ```http
      POST /api/posts/1/like HTTP/1.1
      Host: example.com
      Authorization: Bearer {token}
      ```

    - **响应示例：**

      ```json
      {
        "message": "Post liked successfully"
      }
      ```

4. **关注用户接口**

    - **请求方式：** POST

    - **URL：** /api/users/{userId}/follow

    - **请求示例：**

      ```http
      POST /api/users/2/follow HTTP/1.1
      Host: example.com
      Authorization: Bearer {token}
      ```

    - **响应示例：**

      ```json
      {
        "message": "User followed successfully"
      }
      ```

5. **取消关注接口**

    - **请求方式：** DELETE

    - **URL：** /api/users/{userId}/follow

    - **请求示例：**

      ```http
      DELETE /api/users/2/follow HTTP/1.1
      Host: example.com
      Authorization: Bearer {token}
      ```

    - **响应示例：**

      ```json
      {
        "message": "User unfollowed successfully"
      }
      ```

6. **收藏帖子接口**

    - **请求方式：** POST

    - **URL：** /api/posts/{postId}/favorite

    - **请求示例：**

      ```http
      POST /api/posts/1/favorite HTTP/1.1
      Host: example.com
      Authorization: Bearer {token}
      ```

    - **响应示例：**

      ```json
      {
        "message": "Post favorited successfully"
      }
      ```

7. **取消收藏接口**

    - **请求方式：** DELETE

    - **URL：** /api/posts/{postId}/favorite

    - **请求示例：**

      ```http
      DELETE /api/posts/1/favorite HTTP/1.1
      Host: example.com
      Authorization: Bearer {token}
      ```

    - **响应示例：**

      ```json
      {
        "message": "Post unfavorited successfully"
      }
      ```

### 搜索与发现模块

1. **关键词搜索接口**

    - **请求方式：** GET

    - **URL：** /api/search?q={keyword}

    - **请求示例：**

      ```http
      GET /api/search?q=java HTTP/1.1
      Host: example.com
      ```

    - **响应示例：**

      ```json
      {
        "results": [
          {
            "type": "post",
            "id": 1,
            "title": "Java Programming"
          },
          {
            "type": "user",
            "id": 1,
            "username": "user123"
          }
        ]
      }
      ```

### 通知与消息模块

1. **实时通知接口**

    - **请求方式：** POST

    - **URL：** /api/notifications

    - **请求示例：**

      ```http
      POST /api/notifications HTTP/1.1
      Host: example.com
      Content-Type: application/json
      Authorization: Bearer {token}
      
      {
        "type": "comment",
        "postId": 1,
        "message": "New comment on your post"
      }
      ```

    - **响应示例：**

      ```json
      {
        "message": "Notification sent successfully"
      }
      ```

### 权限管理模块

1. **权限验证接口**

    - **请求方式：** GET

    - **URL：** /api/permissions/{action}

    - **请求示例：**

      ```http
      GET /api/permissions/post:create HTTP/1.1
      Host: example.com
      Authorization: Bearer {token}
      ```

    - **响应示例：**

      ```json
      {
        "allowed": true
      }
      ```

2. **获取所有用户接口**

    - **请求方式：** GET

    - **URL：** /api/users?page={page}&limit={limit}

    - **请求示例：**

      ```http
      GET /api/users?page=1&limit=10 HTTP/1.1
      Host: example.com
      Authorization: Bearer {adminToken}
      ```

    - **响应示例：**

      ```json
      {
        "total": 20,
        "users": [
          {
            "id": 1,
            "username": "user123",
            "email": "user@example.com"
          }
        ]
      }
      ```

3. **分配用户角色接口**

    - **请求方式：** PUT

    - **URL：** /api/users/{userId}/role

    - **请求示例：**

      ```http
      PUT /api/users/2/role HTTP/1.1
      Host: example.com
      Content-Type: application/json
      Authorization: Bearer {adminToken}
      
      {
        "roleId": 1
      }
      ```

    - **响应示例：**

      ```json
      {
        "message": "User role updated successfully"
      }
      ```
