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
        String username = JwtUtil.getUsername(token);
        User user = userService.getUser(username);
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
        String username = JwtUtil.getUsername(token);
        Long userId = userService.getUser(username).getId();
        userService.updateUserByIdAndUpdatedUser(userId, updatedUser);
        return ResponseEntity.ok(new MessageResponse("Profile updated successfully"));
    }

}
