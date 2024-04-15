//package com.bbs.controller;
//
//import com.bbs.entity.User;
//import com.bbs.service.UserService;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//import java.util.List;
//
//@PreAuthorize("hasRole('ADMIN')")
//@RestController
//@RequestMapping("/api/admin")
//public class AdminController {
//    @Resource
//    private UserService userService;
//
//    @GetMapping("/users")
//    public List<User> getAllUsers() {
//        return userService.findAllUsers();
//    }
//
//    @PutMapping("/{userId}/roles/{roleId}")
//    public User assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
//        return userService.assignRoleToUser(userId, roleId);
//    }
//
//}
