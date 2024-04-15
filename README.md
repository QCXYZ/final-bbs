# 接口文档

## 1. 用户认证模块

### 1.1 用户注册接口

- **URL**: `/api/auth/register`

- **Method**: `POST`

- **Auth Required**: No

- **Data Constraints**:

  ```json
  {
    "username": "[valid username]",
    "password": "[password in plain text]",
    "email": "[valid QQ email address]"
  }
  ```

- **Success Response**:

    - **Code**: 200 OK

    - **Content**:

      ```json
      {
        "message": "User registered successfully."
      }
      ```

- **Error Response**:

    - **Code**: 400 BAD REQUEST

    - **Content**:

      ```json
      {
        "error": "Username or email already exists."
      }
      ```

### 1.2 用户登录接口

- **URL**: `/api/auth/login`

- **Method**: `POST`

- **Auth Required**: No

- **Data Constraints**:

  ```json
  {
    "username": "[valid username]",
    "password": "[password in plain text]"
  }
  ```

- **Success Response**:

    - **Code**: 200 OK

    - **Content**:

      ```json
      {
        "token": "[JWT token]",
        "message": "Login successful."
      }
      ```

- **Error Response**:

    - **Code**: 401 UNAUTHORIZED

    - **Content**:

      ```json
      {
        "error": "Invalid username or password."
      }
      ```

### 1.3 忘记密码接口

- **URL**: `/api/auth/forgot-password`

- **Method**: `POST`

- **Auth Required**: No

- **Data Constraints**:

  ```json
  {
    "email": "[registered QQ email address]"
  }
  ```

- **Success Response**:

    - **Code**: 200 OK

    - **Content**:

      ```json
      {
        "message": "Password reset link sent to your email."
      }
      ```

- **Error Response**:

    - **Code**: 404 NOT FOUND

    - **Content**:

      ```json
      {
        "error": "Email not found."
      }
      ```

### 1.4 密码重置接口

- **URL**: `/api/auth/reset-password`

- **Method**: `POST`

- **Auth Required**: No

- **Data Constraints**:

  ```json
  {
    "token": "[reset token]",
    "newPassword": "[new password in plain text]"
  }
  ```

- **Success Response**:

    - **Code**: 200 OK

    - **Content**:

      ```json
      {
        "message": "Password has been reset successfully."
      }
      ```

- **Error Response**:

    - **Code**: 400 BAD REQUEST

    - **Content**:

      ```json
      {
        "error": "Invalid or expired reset token."
      }
      ```

## 2. 用户资料模块

### 2.1 查看个人资料接口

- **URL**: `/api/user/profile`

- **Method**: `GET`

- **Auth Required**: Yes (JWT)

- **Success Response**:

    - **Code**: 200 OK

    - **Content**:

      ```json
      {
        "username": "[username]",
        "email": "[email]",
        "avatar": "[avatar URL]",
        "nickname": "[nickname]",
        "signature": "[personal signature]"
      }
      ```

- **Error Response**:

    - **Code**: 401 UNAUTHORIZED

    - **Content**:

      ```json
      {
        "error": "Authentication failed."
      }
      ```

### 2.2 资料编辑接口

- **URL**: `/api/user/edit-profile`

- **Method**: `PUT`

- **Auth Required**: Yes (JWT)

- **Data Constraints**:

  ```json
  {
    "avatar": "[new avatar URL]",
    "nickname": "[new nickname]",
    "signature": "[new personal signature]"
  }
  ```

- **Success Response**:

    - **Code**: 200 OK

    - **Content**:

      ```json
      {
        "message": "Profile updated successfully."
      }
      ```

- **Error Response**:

    - **Code**: 401 UNAUTHORIZED

    - **Content**:

      ```json
      {
        "error": "Authentication failed."
      }
      ```

## 3. 帖子管理模块

### 3.1 帖子发布接口

- **URL**: `/api/posts/create`

- **Method**: `POST`

- **Auth Required**: Yes (JWT)

- **Data Constraints**:

  ```json
  {
    "title": "[post title]",
    "content": "[post content]",
    "images": "[array of image URLs]",
    "videos": "[array of video URLs]"
  }
  ```

- **Success Response**:

    - **Code**: 201 OK

    - **Content**:

      ```json
      {
        "message": "Post created successfully."
      }
      ```

- **Error Response**:

    - **Code**: 401 UNAUTHORIZED

    - **Content**:

      ```json
      {
        "error": "Authentication failed."
      }
      ```

### 3.2 获取所有帖子接口

- **URL**: `/api/posts`

- **Method**: `GET`

- **Auth Required**: Yes (JWT)

- Parameters

  :

    - `filter`: [optional filter criteria]
    - `sort`: [optional sorting order]
    - `page`: [optional page number for pagination]

- Success Response

  :

    - **Code**: 200 OK

    - Content

      :

      ```json
      {
        "posts": "[array of posts]",
        "totalPages": "[total number of pages]"
      }
      ```

- Error Response

  :

    - **Code**: 404 NOT FOUND

    - Content

      :

      ```json
      {
        "error": "No posts found."
      }
      ```

### 3.3 查看帖子详情接口

- **URL**: `/api/posts/detail`

- **Method**: `GET`

- **Auth Required**: Yes (JWT)

- Parameters

  :

    - `postId`: [required post ID to fetch details]

- Success Response

  :

    - **Code**: 200 OK

    - Content

      :

      ```json
      {
        "post": {
          "title": "[post title]",
          "content": "[post content]",
          "images": "[array of image URLs]",
          "videos": "[array of video URLs]",
          "createdAt": "[creation date]",
          "updatedAt": "[last update date]"
        }
      }
      ```

- Error Response

  :

    - **Code**: 404 NOT FOUND

    - Content

      :

      ```json
      {
        "error": "Post not found."
      }
      ```

### 3.4 帖子编辑接口

- **URL**: `/api/posts/edit`

- **Method**: `PUT`

- **Auth Required**: Yes (JWT)

- **Data Constraints**:

  ```json
  {
    "postId": "[valid post ID]",
    "title": "[new title]",
    "content": "[new content]"
  }
  ```

- **Success Response**:

    - **Code**: 200 OK

    - **Content**:

      ```json
      {
        "message": "Post updated successfully."
      }
      ```

- **Error Response**:

    - **Code**: 404 NOT FOUND

    - **Content**:

      ```json
      {
        "error": "Post not found."
      }
      ```

### 3.5 帖子删除接口

- **URL**: `/api/posts/delete`

- **Method**: `DELETE`

- **Auth Required**: Yes (JWT)

- **Data Constraints**:

  ```json
  {
    "postId": "[valid post ID]"
  }
  ```

- **Success Response**:

    - **Code**: 200 OK

    - **Content**:

      ```json
      {
        "message": "Post deleted successfully."
      }
      ```

- **Error Response**:

    - **Code**: 404 NOT FOUND

    - **Content**:

      ```json
      {
        "error": "Post not found."
      }
      ```

### 3.6 图片视频上传接口

- **URL**: `/api/posts/upload-media`

- **Method**: `POST`

- **Auth Required**: Yes (JWT)

- **Data Constraints**:

  ```json
  {
    "files": "[array of files]"
  }
  ```

- **Success Response**:

    - **Code**: 200 OK

    - **Content**:

      ```json
      {
        "mediaUrls": "[array of media URLs]"
      }
      ```

- **Error Response**:

    - **Code**: 400 BAD REQUEST

    - **Content**:

      ```json
      {
        "error": "Invalid file format or size."
      }
      ```

### 3.7 帖子审核开关接口

- **URL**: `/api/admin/toggle-post-audit`

- **Method**: `PUT`

- **Auth Required**: Yes (JWT)

- **Data Constraints**:

  ```json
  {
    "status": "[audit status enabled or disabled]"
  }
  ```

- **Success Response**:

    - **Code**: 200 OK

    - **Content**:

      ```json
      {
        "message": "Audit status updated successfully."
      }
      ```

- **Error Response**:

    - **Code**: 401 UNAUTHORIZED

    - **Content**:

      ```json
      {
        "error": "Unauthorized access."
      }
      ```

### 3.8 帖子审核接口

- **URL**: `/api/admin/review-post`

- **Method**: `POST`

- **Auth Required**: Yes (JWT)

- **Data Constraints**:

  ```json
  {
    "postId": "[valid post ID]",
    "action": "[approve or reject]",
    "reason": "[reason if rejected]"
  }
  ```

- **Success Response**:

    - **Code**: 200 OK

    - **Content**:

      ```json
      {
        "message": "Post reviewed successfully."
      }
      ```

- **Error Response**:

    - **Code**: 404 NOT FOUND

    - **Content**:

      ```json
      {
        "error": "Post not found."
      }
      ```

## 4. 互动交流模块

### 4.1 发表评论接口

- **URL**: `/api/interaction/comment`

- **Method**: `POST`

- **Auth Required**: Yes (JWT)

- **Data Constraints**:

  ```json
  {
    "postId": "[valid post ID]",
    "content": "[comment content]"
  }
  ```

- **Success Response**:

    - **Code**: 201 OK

    - **Content**:

      ```json
      {
        "message": "Comment posted successfully."
      }
      ```

- **Error Response**:

    - **Code**: 404 NOT FOUND

    - **Content**:

      ```json
      {
        "error": "Post not found."
      }
      ```

### 4.2 删除评论接口

- **URL**: `/api/interaction/delete-comment`

- **Method**: `DELETE`

- **Auth Required**: Yes (JWT)

- **Data Constraints**:

  ```json
  {
    "commentId": "[valid comment ID]"
  }
  ```

- **Success Response**:

    - **Code**: 200 OK

    - **Content**:

      ```json
      {
        "message": "Comment deleted successfully."
      }
      ```

- **Error Response**:

    - **Code**: 404 NOT FOUND

    - **Content**:

      ```json
      {
        "error": "Comment not found."
      }
      ```

### 4.3 点赞或评分接口

- **URL**: `/api/interaction/like-rate`

- **Method**: `POST`

- **Auth Required**: Yes (JWT)

- **Data Constraints**:

  ```json
  {
    "postId": "[valid post ID]",
    "action": "[like or rate]",
    "rating": "[rating value if applicable]"
  }
  ```

- **Success Response**:

    - **Code**: 200 OK

    - **Content**:

      ```json
      {
        "message": "Action performed successfully."
      }
      ```

- **Error Response**:

    - **Code**: 404 NOT FOUND

    - **Content**:

      ```json
      {
        "error": "Post not found."
      }
      ```

### 4.4 关注用户接口

- **URL**: `/api/interaction/follow-user`

- **Method**: `POST`

- **Auth Required**: Yes (JWT)

- **Data Constraints**:

  ```json
  {
    "userId": "[user ID to follow]"
  }
  ```

- **Success Response**:

    - **Code**: 200 OK

    - **Content**:

      ```json
      {
        "message": "User followed successfully."
      }
      ```

- **Error Response**:

    - **Code**: 404 NOT FOUND

    - **Content**:

      ```json
      {
        "error": "User not found."
      }
      ```

### 4.5 取消关注接口

- **URL**: `/api/interaction/unfollow-user`

- **Method**: `POST`

- **Auth Required**: Yes (JWT)

- **Data Constraints**:

  ```json
  {
    "userId": "[user ID to unfollow]"
  }
  ```

- **Success Response**:

    - **Code**: 200 OK

    - **Content**:

      ```json
      {
        "message": "User unfollowed successfully."
      }
      ```

- **Error Response**:

    - **Code**: 404 NOT FOUND

    - **Content**:

      ```json
      {
        "error": "User not found."
      }
      ```

### 4.6 收藏帖子接口

- **URL**: `/api/interaction/favorite-post`

- **Method**: `POST`

- **Auth Required**: Yes (JWT)

- **Data Constraints**:

  ```json
  {
    "postId": "[post ID to favorite]"
  }
  ```

- **Success Response**:

    - **Code**: 200 OK

    - **Content**:

      ```json
      {
        "message": "Post favorited successfully."
      }
      ```

- **Error Response**:

    - **Code**: 404 NOT FOUND

    - **Content**:

      ```json
      {
        "error": "Post not found."
      }
      ```

### 4.7 取消收藏接口

- **URL**: `/api/interaction/unfavorite-post`

- **Method**: `POST`

- **Auth Required**: Yes (JWT)

- **Data Constraints**:

  ```json
  {
    "postId": "[post ID to unfavorite]"
  }
  ```

- **Success Response**:

    - **Code**: 200 OK

    - **Content**:

      ```json
      {
        "message": "Post unfavorited successfully."
      }
      ```

- **Error Response**:

    - **Code**: 404 NOT FOUND

    - **Content**:

      ```json
      {
        "error": "Post not found."
      }
      ```

## 5. 搜索与发现模块

### 5.1 关键词搜索接口

- **URL**: `/api/search`

- **Method**: `GET`

- **Auth Required**: No

- **Parameters**:

    - `q`: [search query]

- **Success Response**:

    - **Code**: 200 OK

    - **Content**:

      ```json
      {
        "results": "[search results]"
      }
      ```

- **Error Response**:

    - **Code**: 400 BAD REQUEST

    - **Content**:

      ```json
      {
        "error": "Invalid search query."
      }
      ```

## 6. 通知与消息模块

### 6.1 实时通知接口

- **URL**: `/api/notifications`

- **Method**: `GET`

- **Auth Required**: Yes (JWT)

- **Success Response**:

    - **Code**: 200 OK

    - **Content**:

      ```json
      {
        "notifications": "[list of notifications]"
      }
      ```

- **Error Response**:

    - **Code**: 401 UNAUTHORIZED

    - **Content**:

      ```json
      {
        "error": "Authentication failed."
      }
      ```

## 7. 权限管理模块

### 7.1 权限验证接口

- **URL**: `/api/permissions/verify`

- **Method**: `GET`

- **Auth Required**: Yes (JWT)

- **Success Response**:

    - **Code**: 200 OK

    - **Content**:

      ```json
      {
        "message": "Permission verified."
      }
      ```

- **Error Response**:

    - **Code**: 403 FORBIDDEN

    - **Content**:

      ```json
      {
        "error": "Insufficient permissions."
      }
      ```

### 7.2 获取所有角色接口

- **URL**: `/api/roles`

- **Method**: `GET`

- **Auth Required**: Yes (JWT)

- **Success Response**:

    - **Code**: 200 OK

    - **Content**:

      ```json
      {
        "roles": "[list of roles]"
      }
      ```

- **Error Response**:

    - **Code**: 401 UNAUTHORIZED

    - **Content**:

      ```json
      {
        "error": "Authentication failed."
      }
      ```

### 7.3 分配用户角色接口

- **URL**: `/api/roles/assign`

- **Method**: `POST`

- **Auth Required**: Yes (JWT)

- **Data Constraints**:

  ```json
  {
    "userId": "[user ID]",
    "role": "[role to assign]"
  }
  ```

- **Success Response**:

    - **Code**: 200 OK

    - **Content**:

      ```json
      {
        "message": "Role assigned successfully."
      }
      ```

- **Error Response**:

    - **Code**: 404 NOT FOUND

    - **Content**:

      ```json
      {
        "error": "User not found."
      }
      ```

    - **Additional Error**:

        - **Code**: 403 FORBIDDEN

        - **Content**:

          ```json
          {
            "error": "Insufficient permissions to assign role."
          }
          ```

This detailed documentation provides a comprehensive overview of the APIs available for the different functionalities of
the system, including user management, post interactions, and permission controls. It ensures that developers have clear
instructions on how to integrate and utilize these interfaces effectively.
