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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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

    // 发布帖子
    public void createPost(String username, Post post) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        postRepository.save(post);
    }

    // 编辑帖子
    public void updatePost(Long postId, String title, String content, HttpServletRequest request) {
        User user = jwtUtil.getCurrentUser(request);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        if (!post.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied");
        }

        post.setTitle(title);
        post.setContent(content);
        post.setUpdatedAt(LocalDateTime.now());
        postRepository.save(post);
    }

    // 删除帖子
    public void deletePost(Long postId, HttpServletRequest request) {
        User user = jwtUtil.getCurrentUser(request);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        if (!post.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied");
        }

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
//        userRepository.findById(user.getId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
        comment.setPost(post);
        comment.setUser(user);  // 设置用户
        commentRepository.save(comment);
        return ResponseEntity.ok("Comment posted successfully");
    }

    // 删除自己的评论
    public ResponseEntity<?> deleteComment(HttpServletRequest request, Long commentId) {
        User user = jwtUtil.getCurrentUser(request);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        if (!comment.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only delete your own comments.");
        }
        commentRepository.delete(comment);
        return ResponseEntity.ok("Comment deleted successfully");
    }

    // 点赞/取消点赞帖子
    public ResponseEntity<?> likePost(HttpServletRequest request, Long postId) {
        User user = jwtUtil.getCurrentUser(request);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        if (post.getLikes().contains(user)) {
            post.getLikes().remove(user);
            post.setLikeCount(Optional.ofNullable(post.getLikeCount()).orElse(0) - 1);
            postRepository.save(post);
            return ResponseEntity.ok("Post unliked successfully");
        } else {
            post.getLikes().add(user);
            post.setLikeCount(Optional.ofNullable(post.getLikeCount()).orElse(0) + 1);
            postRepository.save(post);
            return ResponseEntity.ok("Post liked successfully");
        }
    }

    // 收藏/取消收藏帖子
    public ResponseEntity<?> favoritePost(HttpServletRequest request, Long postId) {
        User user = jwtUtil.getCurrentUser(request);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        Favorite favorite = favoriteRepository.findByUserIdAndPostId(user.getId(), postId);
        if (favorite != null) {
            favoriteRepository.delete(favorite);
            return ResponseEntity.ok("Post unfavorited successfully");
        } else {
            Favorite newFavorite = new Favorite();
            newFavorite.setPost(post);
            newFavorite.setUser(user);
            favoriteRepository.save(newFavorite);
            return ResponseEntity.ok("Post favorited successfully");
        }
    }

}
