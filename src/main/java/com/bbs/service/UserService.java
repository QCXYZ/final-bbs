package com.bbs.service;

import com.bbs.entity.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);

    User assignRoleToUser(Long userId, Long roleId);

    List<User> findAllUsers();
}
