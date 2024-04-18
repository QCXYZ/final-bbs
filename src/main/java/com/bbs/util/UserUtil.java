package com.bbs.util;

import com.bbs.entity.User;
import com.bbs.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Component
public class UserUtil {
    @Resource
    private UserService userService;

    public User getCurrentUser(HttpServletRequest request) {
        String jwt = request.getHeader("Authorization");
        String username = JwtUtil.getUsername(jwt);
        return userService.getUser(username);
    }

    public Long getUserIdFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // 去掉前缀 "Bearer "
            if (JwtUtil.validateToken(token)) {
                String username = JwtUtil.getUsername(token);
                // 现在我们有了用户名，需要查询数据库以获取用户ID
                User user = userService.getUser(username);
                return user.getId(); // 返回用户ID
            }
        }
        return null; // 如果token无效或用户不存在，则返回null
    }
}
