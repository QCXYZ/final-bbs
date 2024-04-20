package com.bbs.controller;

import com.bbs.entity.User;
import com.bbs.service.UserService;
import com.bbs.util.JwtUtil;
import com.bbs.util.R;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @PostMapping("/register")
    public R<?> registerUser(@RequestBody User user) {
        userService.registerUser(user);
        return R.ok(null);
    }

    @PostMapping("/login")
    public R<?> loginUser(@RequestBody Map<String, String> requestBody) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestBody.get("username"), requestBody.get("password"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        UserDetails userDetails = userService.loadUserByUsername(requestBody.get("username"));
        return R.ok(JwtUtil.generateToken(userDetails));
    }

    @PostMapping("/forgot-password")
    public R<?> forgotPassword(@RequestBody Map<String, String> requestBody) {
        userService.generateResetTokenAndSendEmail(requestBody.get("email"));
        return R.ok(null);
    }

    @PostMapping("/reset-password")
    public R<?> resetPassword(@RequestBody Map<String, String> requestBody) {
        userService.resetPassword(requestBody.get("token"), requestBody.get("newPassword"));
        return R.ok(null);
    }

}
