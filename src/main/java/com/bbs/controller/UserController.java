package com.bbs.controller;

import com.bbs.dto.MessageResponse;
import com.bbs.entity.User;
import com.bbs.service.UserService;
import com.bbs.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String token) {
        Long userId = getUserIdFromToken(token);
        User user = userService.getUserProfile(userId);
        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "avatar", user.getAvatar(),
                "nickname", user.getNickname(),
                "signature", user.getSignature(),
                "role", user.getRole().getName()));
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@RequestHeader("Authorization") String token, @RequestBody User updatedUser) {
        Long userId = getUserIdFromToken(token);
        userService.updateUserProfile(userId, updatedUser);
        return ResponseEntity.ok(new MessageResponse("Profile updated successfully"));
    }

    public Long getUserIdFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // 去掉前缀 "Bearer "
            if (JwtUtil.validateToken(token)) {
                String username = JwtUtil.getUsernameFromToken(token);
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
