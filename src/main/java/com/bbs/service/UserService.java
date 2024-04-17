package com.bbs.service;

import com.bbs.entity.User;
import com.bbs.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    @Resource
    private UserRepository userRepository;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private EmailService emailService;

    public void registerUser(User user) {
        // 检查用户是否已存在
        if (userRepository.existsByUsernameOrEmail(user.getUsername(), user.getEmail())) {
            throw new RuntimeException("用户名或邮箱已存在.");
        }
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 保存用户
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(()
                -> new UsernameNotFoundException("未找到用户名为：" + username + " 的用户"));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName())));
    }

    public void generateResetTokenAndSendEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not found."));
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        // 设置令牌过期时间为1小时后
        user.setResetTokenExpiryDate(new Date(System.currentTimeMillis() + 3600 * 1000));
        userRepository.save(user);
        emailService.sendResetTokenEmail(email, token);
    }

    public void resetPassword(String token, String newPassword) {
        Optional<User> userOptional = userRepository.findByResetToken(token);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Invalid or expired reset token.");
        }
        User user = userOptional.get();
        // 检查令牌是否过期
        if (new Date().after(user.getResetTokenExpiryDate())) {
            throw new RuntimeException("Invalid or expired reset token.");
        }
        // 重置密码
        user.setPassword(passwordEncoder.encode(newPassword));
        // 清除令牌
        user.setResetToken(null);
        user.setResetTokenExpiryDate(null);
        userRepository.save(user);
    }

    // 用户资料模块
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found."));
    }

    @Transactional
    public void updateUserByIdAndUpdatedUser(Long userId, User updatedUser) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found."));
        user.setAvatar(updatedUser.getAvatar());
        user.setNickname(updatedUser.getNickname());
        user.setSignature(updatedUser.getSignature());
        userRepository.save(user);
    }

}
