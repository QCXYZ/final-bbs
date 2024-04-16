package com.bbs.controller;

import com.bbs.dto.MessageResponse;
import com.bbs.entity.User;
import com.bbs.service.UserService;
import com.bbs.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String token) {
        Long userId = getUserIdFromToken(token);
        User user = userService.getUserProfile(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@RequestHeader("Authorization") String token, @RequestBody User updatedUser) {
        Long userId = getUserIdFromToken(token);
        userService.updateUserProfile(userId, updatedUser);
        return ResponseEntity.ok(new MessageResponse("Profile updated successfully"));
    }

    private Long getUserIdFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // 去掉前缀 "Bearer "
            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.getUsernameFromToken(token);
                // 现在我们有了用户名，需要查询数据库以获取用户ID
                User user = userService.findByUsername(username);
                if (user != null) {
                    return user.getId(); // 返回用户ID
                }
            }
        }
        return null; // 如果token无效或用户不存在，则返回null
    }

}
