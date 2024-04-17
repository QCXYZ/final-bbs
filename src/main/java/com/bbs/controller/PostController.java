package com.bbs.controller;

import com.bbs.entity.Post;
import com.bbs.service.ConfigurationService;
import com.bbs.service.MediaService;
import com.bbs.service.PostService;
import com.bbs.util.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Resource
    private PostService postService;
    @Resource
    private MediaService mediaService;
    @Resource
    private ConfigurationService configurationService;

    // 发布帖子
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody Post post, @RequestHeader("Authorization") String token) {
        String username = JwtUtil.getUsername(token.substring(7));
        postService.createPost(username, post);
        return new ResponseEntity<>(Map.of("message", "Post created successfully"), HttpStatus.CREATED);
    }

    // 获取所有已审核的帖子
    @GetMapping("/reviewed")
    public ResponseEntity<?> getAllReviewedPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        Page<Post> posts = postService.getAllReviewedPosts(page - 1, limit);
        Map<String, Object> response = new HashMap<>();
        response.put("total", posts.getTotalElements());
        response.put("posts", posts.getContent());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 管理员获取所有未审核的帖子
    @GetMapping("/unreviewed")
    public ResponseEntity<?> getAllUnreviewedPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestHeader("Authorization") String adminToken) {
        // 这里应该验证管理员权限
        Page<Post> posts = postService.getAllUnreviewedPosts(page - 1, limit);
        Map<String, Object> response = new HashMap<>();
        response.put("total", posts.getTotalElements());
        response.put("posts", posts.getContent());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 查看帖子详情
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(@PathVariable Long postId) {
        Post post = postService.getPost(postId);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    // 编辑帖子
    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable Long postId, @RequestBody Map<String, String> updates, @RequestHeader("Authorization") String token) {
        JwtUtil.getUsername(token.substring(7));
        postService.updatePost(postId, updates.get("title"), updates.get("content"));
        return new ResponseEntity<>(Map.of("message", "Post updated successfully"), HttpStatus.OK);
    }

    // 删除帖子
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId, @RequestHeader("Authorization") String token) {
        JwtUtil.getUsername(token.substring(7));
        postService.deletePost(postId);
        return new ResponseEntity<>(Map.of("message", "Post deleted successfully"), HttpStatus.OK);
    }

    // 上传帖子的媒体文件
    @PostMapping("/{postId}/media")
    public ResponseEntity<?> uploadMedia(@PathVariable Long postId,
                                         @RequestParam("media") MultipartFile[] files,
                                         @RequestHeader("Authorization") String token) throws IOException {
        JwtUtil.getUsername(token.substring(7));
        List<String> mediaUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileUrl = mediaService.saveFile(file);
            mediaUrls.add(fileUrl);
        }

        mediaService.addMediaToPost(postId, mediaUrls);

        return new ResponseEntity<>(Map.of("message", "Media uploaded successfully"), HttpStatus.OK);
    }

    // 设置帖子审核开关
    @PutMapping("/review/switch")
    public ResponseEntity<?> setReviewSwitch(@RequestBody Map<String, Boolean> payload,
                                             @RequestHeader("Authorization") String adminToken) {
        // 这里应该验证管理员权限
        configurationService.setReviewSwitch(payload.get("enabled"));
        return new ResponseEntity<>(Map.of("message", "Post review switch updated successfully"), HttpStatus.OK);
    }

    // 审核帖子
    @PutMapping("/{postId}/review")
    public ResponseEntity<?> reviewPost(@PathVariable Long postId,
                                        @RequestBody Map<String, Boolean> payload,
                                        @RequestHeader("Authorization") String adminToken) {
        // 验证管理员权限
        postService.setPostReview(postId, payload.get("approved"));
        return new ResponseEntity<>(Map.of("message", "Post reviewed successfully"), HttpStatus.OK);
    }

}
