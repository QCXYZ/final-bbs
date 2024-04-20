package com.bbs.service;

import com.bbs.entity.Comment;
import com.bbs.entity.Favorite;
import com.bbs.entity.Post;
import com.bbs.entity.User;
import com.bbs.repository.CommentRepository;
import com.bbs.repository.FavoriteRepository;
import com.bbs.repository.PostRepository;
import com.bbs.util.UserUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private ConfigurationService configurationService;
    @Resource
    private CommentRepository commentRepository;
    @Resource
    private FavoriteRepository favoriteRepository;
    @Resource
    private UserUtil userUtil;

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

    // 发布帖子
    public void createPost(HttpServletRequest request, Post post) {
        post.setUser(userUtil.getCurrentUser(request));
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        postRepository.save(post);
    }

    // 获取帖子详情
    public Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
    }

    // 编辑帖子
    public void updatePost(Long postId, String title, String content, HttpServletRequest request) {
        Post post = getPost(postId);
        if (!post.getUser().getId().equals(userUtil.getCurrentUserId(request))) {
            throw new AccessDeniedException("Access denied");
        }

        post.setTitle(title);
        post.setContent(content);
        post.setUpdatedAt(LocalDateTime.now());
        postRepository.save(post);
    }

    // 删除帖子
//    @Transactional 不用加这个注解，因为在service层，调用了repository的delete或save方法，这些方法已经加了这个注解
    @Transactional // 但是如果是多个delete或save，必须加这个注解，否则会报错(报错原因是因为存在多个事务，而事务的传播行为默认是REQUIRED，所以会报错)
    // 事务的传播行为：REQUIRED：如果当前没有事务，就新建一个事务，如果已经存在一个事务中，加入到这个事务中
    public void deletePost(Long postId, HttpServletRequest request) {
        Post post = getPost(postId);
        User user = userUtil.getCurrentUser(request);
        if (!post.getUser().getId().equals(user.getId()) &&
                !user.getRole().getName().equals("ADMIN")) {
            throw new AccessDeniedException("Access denied");
        }

        commentRepository.deleteByPostId(postId);
        favoriteRepository.deleteByPostId(postId);
        postRepository.delete(post);
    }

    // 提交审核
    public void setPostReview(Long postId, boolean approved) {
        if (!configurationService.getReviewSwitch()) {
            throw new IllegalStateException("Review process is disabled.");
        }

        Post post = getPost(postId);
        post.setReviewed(approved);
        postRepository.save(post);
    }


    // 互动交流模块
    // 添加评论
    public void addComment(HttpServletRequest request, Long postId, Comment comment) {
        Post post = getPost(postId);
        comment.setPost(post);
        comment.setUser(userUtil.getCurrentUser(request));  // 设置用户
        commentRepository.save(comment);
    }

    // 删除自己的评论
    public void deleteComment(HttpServletRequest request, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        if (!comment.getUser().getId().equals(userUtil.getCurrentUser(request).getId())) {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only delete your own comments.");
            return;
        }
        commentRepository.delete(comment);
    }

    // 点赞/取消点赞帖子
    public void likePost(HttpServletRequest request, Long postId) {
        Post post = getPost(postId);
        User user = userUtil.getCurrentUser(request);
        if (post.getLikes().contains(user)) {
            post.getLikes().remove(user);
            post.setLikeCount(Optional.ofNullable(post.getLikeCount()).orElse(0) - 1);
            postRepository.save(post);
            return;
        }
        post.getLikes().add(user);
        post.setLikeCount(Optional.ofNullable(post.getLikeCount()).orElse(0) + 1);
        postRepository.save(post);
    }

    // 收藏/取消收藏帖子
    public void favoritePost(HttpServletRequest request, Long postId) {
        Post post = getPost(postId);
        User user = userUtil.getCurrentUser(request);
        Favorite favorite = favoriteRepository.findByUserIdAndPostId(user.getId(), postId);
        if (favorite != null) {
            favoriteRepository.delete(favorite);
            return;
        }
        Favorite newFavorite = new Favorite();
        newFavorite.setPost(post);
        newFavorite.setUser(user);
        favoriteRepository.save(newFavorite);
    }

}
