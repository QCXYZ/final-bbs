package com.bbs.service;

import com.bbs.entity.PasswordResetToken;
import com.bbs.entity.User;
import com.bbs.repository.PasswordResetTokenRepository;
import com.bbs.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Resource
    private UserRepository userRepository;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(()
                -> new UsernameNotFoundException("未找到用户名为：" + username + " 的用户"));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), Collections.emptyList());
    }

    public User registerUser(User user) {
        // 检查用户是否已存在
        if (userRepository.existsByUsernameOrEmail(user.getUsername(), user.getEmail())) {
            throw new RuntimeException("用户名或邮箱已存在.");
        }
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 保存用户
        return userRepository.save(user);
    }

    public boolean checkUserExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void updateUserPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("未找到邮箱为：" + email + " 的用户"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void createPasswordResetTokenForUser(final User user, final String token) {
        final PasswordResetToken myToken = new PasswordResetToken();
        myToken.setToken(token);
        myToken.setUser(user);
        myToken.setExpiryDate(LocalDateTime.now().plusHours(2)); // 令牌有效期为2小时
        passwordResetTokenRepository.save(myToken);
    }

    public boolean validatePasswordResetToken(String token) {
        final Optional<PasswordResetToken> passToken = passwordResetTokenRepository.findByToken(token);
        return passToken.isPresent() && passToken.get().getExpiryDate().isAfter(LocalDateTime.now());
    }
}
