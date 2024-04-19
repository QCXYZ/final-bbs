package com.bbs.util;

import com.bbs.entity.User;
import com.bbs.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Component
public class UserUtil {
    @Resource
    private UserService userService;

    public User getCurrentUser(HttpServletRequest request) {
        String jwt = request.getHeader("Authorization");
        String username = JwtUtil.getUsername(jwt);
        return userService.getUser(username);
    }

    public Long getCurrentUserId(HttpServletRequest request) {
        return getCurrentUser(request).getId();
    }
}
