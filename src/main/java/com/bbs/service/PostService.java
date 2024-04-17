package com.bbs.service;

import com.bbs.entity.Post;
import com.bbs.entity.User;
import com.bbs.repository.PostRepository;
import com.bbs.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Service
public class PostService {
    @Resource
    private PostRepository postRepository;
    @Resource
    private UserRepository userRepository;
    @Resource
    private ConfigurationService configurationService;

    // 发布帖子
    public void createPost(String username, Post post) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        postRepository.save(post);
    }

    // 获取所有已审核的帖子
    public Page<Post> getAllReviewedPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findAllByReviewed(true, pageable);
    }

    // 获取所有未审核的帖子（仅管理员使用）
    public Page<Post> getAllUnreviewedPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findAllByReviewed(false, pageable);
    }

    // 获取帖子详情
    public Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
    }

    // 编辑帖子
    public void updatePost(Long postId, String title, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        post.setTitle(title);
        post.setContent(content);
        post.setUpdatedAt(LocalDateTime.now());
        postRepository.save(post);
    }

    // 删除帖子
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        postRepository.delete(post);
    }

    // 提交审核
    public void setPostReview(Long postId, boolean approved) {
        if (!configurationService.getReviewSwitch()) {
            throw new IllegalStateException("Review process is disabled.");
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        post.setReviewed(approved);
        postRepository.save(post);
    }

}
