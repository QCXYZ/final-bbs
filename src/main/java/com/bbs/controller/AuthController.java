package com.bbs.controller;

import com.bbs.entity.User;
import com.bbs.service.UserService;
import com.bbs.util.JwtTokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Resource
    private UserService userService;
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(Map.of("message", "用户注册成功."));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> requestBody) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestBody.get("username"), requestBody.get("password"))
        );
        final String token = jwtTokenUtil.generateToken((UserDetails) authentication.getPrincipal());
        return ResponseEntity.ok(Map.of("token", token, "message", "登录成功."));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> requestBody) {
        if (!userService.checkUserExistsByEmail(requestBody.get("email"))) {
            return ResponseEntity.status(404).body(Map.of("error", "请输入注册时的邮箱."));
        }
        // 发送密码重置链接的逻辑在这里
        return ResponseEntity.ok(Map.of("message", "密码重置链接已发送至您的邮箱."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> requestBody) {
        // 假设令牌验证逻辑已在其他地方实现
        userService.updateUserPassword(requestBody.get("email"), requestBody.get("newPassword"));
        return ResponseEntity.ok(Map.of("message", "密码已成功重置."));
    }
}
