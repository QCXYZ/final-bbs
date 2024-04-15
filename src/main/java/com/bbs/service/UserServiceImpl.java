package com.bbs.service;

import com.bbs.entity.Role;
import com.bbs.entity.User;
import com.bbs.repository.RoleRepository;
import com.bbs.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Resource
    private UserRepository userRepository;
    @Resource
    private RoleRepository roleRepository;
    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // 确保密码已编码
        return userRepository.save(user);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("找不到用户"));

        // 在这里，我们再次对密码进行编码，如果在saveUser方法中已经编码过，则不必要
        // 应调整此行以避免重新编码
        // 为简单起见，最好在这里删除密码编码，并依赖saveUser方法中进行的编码
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), // 直接使用编码后的密码
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName())));
    }

    @Override
    public User assignRoleToUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("找不到用户"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("找不到角色"));
        user.setRole(role);
        return userRepository.save(user);
    }

}
