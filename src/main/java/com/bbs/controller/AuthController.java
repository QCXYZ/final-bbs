package com.bbs.controller;

import com.bbs.entity.PasswordResetToken;
import com.bbs.entity.User;
import com.bbs.repository.PasswordResetTokenRepository;
import com.bbs.repository.UserRepository;
import com.bbs.service.EmailService;
import com.bbs.service.UserService;
import com.bbs.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Resource
    private UserService userService;
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private EmailService emailService;
    @Resource
    private UserRepository userRepository;
    @Resource
    private PasswordResetTokenRepository passwordResetTokenRepository;

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
        User user = userRepository.findByEmail(requestBody.get("email"))
                .orElseThrow(() -> new RuntimeException("User not found with email: " + requestBody.get("email")));

        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);
        emailService.sendSimpleMessage(user.getEmail(), "Reset Password", "Here is your password reset token: " + token);

        return ResponseEntity.ok(Map.of("message", "Password reset link sent to your email."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> requestBody) {
        String token = requestBody.get("token");
        if (!userService.validatePasswordResetToken(token)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid or expired reset token."));
        }

        User user = passwordResetTokenRepository.findByToken(token)
                .map(PasswordResetToken::getUser)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        userService.updateUserPassword(user.getEmail(), requestBody.get("newPassword"));
        return ResponseEntity.ok(Map.of("message", "Password has been reset successfully."));
    }
}
