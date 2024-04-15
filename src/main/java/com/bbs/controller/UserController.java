package com.bbs.controller;

import com.bbs.entity.User;
import com.bbs.service.UserServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Resource
    private UserServiceImpl userService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PostMapping("/login")
    public User login(@RequestBody User user) {
        return userService.getUser(user);
    }

}