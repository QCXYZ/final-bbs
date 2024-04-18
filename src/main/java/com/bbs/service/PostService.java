package com.bbs.service;

import com.bbs.entity.Comment;
import com.bbs.entity.Favorite;
import com.bbs.entity.Post;
import com.bbs.entity.User;
import com.bbs.repository.CommentRepository;
import com.bbs.repository.FavoriteRepository;
import com.bbs.repository.PostRepository;
import com.bbs.repository.UserRepository;
import com.bbs.util.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PostService {
    @Resource
    private PostRepository postRepository;
    @Resource
    private UserRepository userRepository;
    @Resource
    private ConfigurationService configurationService;
    @Resource
    private CommentRepository commentRepository;
    @Resource
    private FavoriteRepository favoriteRepository;
    @Resource
    private JwtUtil jwtUtil;

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


    // 互动交流模块
    // 添加评论
    public ResponseEntity<?> addComment(HttpServletRequest request, Long postId, Comment comment) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = jwtUtil.getCurrentUser(request);
        comment.setPost(post);
        comment.setUser(user);  // 设置用户
        commentRepository.save(comment);
        return ResponseEntity.ok("Comment posted successfully");
    }

    // 删除评论
    public ResponseEntity<?> deleteComment(Long postId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null || !comment.getPost().getId().equals(postId)) {
            return ResponseEntity.badRequest().body("Comment not found");
        }
        commentRepository.delete(comment);
        return ResponseEntity.ok("Comment deleted successfully");
    }

    // 点赞帖子
    public ResponseEntity<?> likePost(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            return ResponseEntity.badRequest().body("Post not found");
        }
//        post.setLikeCount(post.getLikeCount() + 1);
        /*
        int likeCount = post.getLikeCount() != null ? post.getLikeCount() : 0;
        post.setLikeCount(likeCount + 1);
        */
        post.setLikeCount(Optional.ofNullable(post.getLikeCount()).orElse(0) + 1);
        postRepository.save(post);
        return ResponseEntity.ok("Post liked successfully");
    }

    // 收藏帖子
    public ResponseEntity<?> favoritePost(HttpServletRequest request, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = jwtUtil.getCurrentUser(request);
        Favorite favorite = new Favorite();
        favorite.setPost(post);
        favorite.setUser(user);  // 设置用户
        favoriteRepository.save(favorite);
        return ResponseEntity.ok("Post favorited successfully");
    }

    // 取消收藏帖子
    public ResponseEntity<?> unfavoritePost(Long postId) {
        Favorite favorite = favoriteRepository.findByPostId(postId).orElse(null);
        if (favorite == null) {
            return ResponseEntity.badRequest().body("Post not favorited yet");
        }
        favoriteRepository.delete(favorite);
        return ResponseEntity.ok("Post unfavorited successfully");
    }

}
