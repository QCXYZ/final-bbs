package com.bbs.controller;

import com.bbs.entity.Post;
import com.bbs.service.PostService;
import com.bbs.util.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Resource
    private PostService postService;

    // 发布帖子
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody Post post, @RequestHeader("Authorization") String token) {
        String username = JwtUtil.getUsername(token.substring(7));
        Post createdPost = postService.createPost(username, post);
        return new ResponseEntity<>(Map.of("message", "Post created successfully"), HttpStatus.CREATED);
    }

    // 获取所有帖子
    @GetMapping
    public ResponseEntity<?> getAllPosts(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int limit) {
        Page<Post> posts = postService.getAllPosts(page - 1, limit);
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
        String username = JwtUtil.getUsername(token.substring(7));
        Post updatedPost = postService.updatePost(postId, updates.get("title"), updates.get("content"));
        return new ResponseEntity<>(Map.of("message", "Post updated successfully"), HttpStatus.OK);
    }

    // 删除帖子
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId, @RequestHeader("Authorization") String token) {
        String username = JwtUtil.getUsername(token.substring(7));
        postService.deletePost(postId);
        return new ResponseEntity<>(Map.of("message", "Post deleted successfully"), HttpStatus.OK);
    }
}
