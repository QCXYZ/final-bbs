package com.bbs.service;

import com.bbs.entity.Role;
import com.bbs.entity.User;
import com.bbs.repository.RoleRepository;
import com.bbs.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserRepository userRepository;
    @Resource
    private RoleRepository roleRepository;

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User assignRoleToUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId).orElse(null);
        Role role = roleRepository.findById(roleId).orElse(null);
        if (user != null && role != null) {
            user.setRole(role);
            userRepository.save(user);
        }
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

}
