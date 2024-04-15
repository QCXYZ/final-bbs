package com.bbs.service;

import com.bbs.entity.User;
import com.bbs.repository.RoleRepository;
import com.bbs.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    @Resource
    private UserRepository userRepository;
    @Resource
    private RoleRepository roleRepository;
    @Resource
    private PasswordEncoder passwordEncoder;

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
}
