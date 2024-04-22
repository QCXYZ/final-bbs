package com.bbs.controller;

import com.bbs.aop.LogAnnotation;
import com.bbs.entity.User;
import com.bbs.service.UserService;
import com.bbs.util.R;
import com.bbs.util.UserUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private UserUtil userUtil;

    @LogAnnotation("获取用户信息")
    @GetMapping("/profile")
    public R<?> getUserProfile(HttpServletRequest request) {
        User user = userUtil.getCurrentUser(request);
        return R.ok(Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "avatar", user.getAvatar(),
                "nickname", user.getNickname(),
                "signature", user.getSignature(),
                "role", user.getRole().getName()
        ));
    }

    @PutMapping("/profile")
    public R<?> updateUserProfile(@RequestBody User updatedUser, HttpServletRequest request) {
        userService.update(userUtil.getCurrentUserId(request), updatedUser);
        return R.ok(null);
    }

}
