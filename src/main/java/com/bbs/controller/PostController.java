package com.bbs.controller;

import com.bbs.aop.LogAnnotation;
import com.bbs.entity.Comment;
import com.bbs.entity.Post;
import com.bbs.service.ConfigurationService;
import com.bbs.service.MediaService;
import com.bbs.service.PostService;
import com.bbs.util.R;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin
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
//    @PreAuthorize("authenticated") "request"中已经包含了用户信息，无需再次验证
    @PostMapping
    public R<?> createPost(@RequestBody Post post,
                           HttpServletRequest request) {
        postService.createPost(request, post);
        return R.ok(null);
    }

    // Spring AOP 在运行时为 PostController 类创建一个代理对象，这个代理对象拦截了
    // getAllReviewedPosts() 方法的调用，
    // 并在调用该方法前执行 LogAspect 类中定义的 logAround() 方法
    @LogAnnotation("获取所有已审核的帖子")
    @GetMapping("/reviewed")
    public R<?> getAllReviewedPosts(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10") int limit) {
        Page<Post> posts = postService.getAllReviewedPosts(page - 1, limit);
        return R.ok(Map.of(
                "total", posts.getTotalElements(),
                "posts", posts.getContent()
        ));
    }

    // 管理员获取所有未审核的帖子
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/unreviewed")
    public R<?> getAllUnreviewedPosts(@RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "10") int limit) {
        Page<Post> posts = postService.getAllUnreviewedPosts(page - 1, limit);
        return R.ok(Map.of(
                "total", posts.getTotalElements(),
                "posts", posts.getContent()
        ));
    }

    // 查看帖子详情
    @GetMapping("/{postId}")
    public R<?> getPost(@PathVariable Long postId) {
        Post post = postService.getPost(postId);
        return R.ok(post);
    }

    // 编辑帖子
//    @PreAuthorize("authenticated")
    @PutMapping("/{postId}")
    public R<?> updatePost(@PathVariable Long postId,
                           @RequestBody Map<String, String> updates,
                           HttpServletRequest request) {
        postService.updatePost(postId, updates.get("title"), updates.get("content"), request);
        return R.ok(null);
    }

    // 删除帖子
//    @PreAuthorize("authenticated")
    @DeleteMapping("/{postId}")
    public R<?> deletePost(@PathVariable Long postId,
                           HttpServletRequest request) {
        postService.deletePost(postId, request);
        return R.ok(null);
    }

    // 上传帖子的媒体文件
    @PreAuthorize("authenticated")
    @PostMapping("/{postId}/media")
    public R<?> uploadMedia(@PathVariable Long postId,
                            @RequestParam("media") MultipartFile[] files) throws IOException {
        List<String> mediaUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileUrl = mediaService.saveFile(file);
            mediaUrls.add(fileUrl);
        }

        mediaService.addMediaToPost(postId, mediaUrls);

        return R.ok(mediaUrls);
    }

    // 设置帖子审核开关
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/review/switch")
    public R<?> setReviewSwitch(@RequestBody Map<String, Boolean> payload) {
        configurationService.setReviewSwitch(payload.get("enabled"));
        return R.ok(null);
    }

    // 审核帖子
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{postId}/review")
    public R<?> reviewPost(@PathVariable Long postId,
                           @RequestBody Map<String, Boolean> payload) {
        postService.setPostReview(postId, payload.get("approved"));
        return R.ok(null);
    }


    // 互动交流模块
//    @PreAuthorize("authenticated")
    @PostMapping("/{postId}/comments")
    public R<?> addComment(HttpServletRequest request,
                           @PathVariable Long postId,
                           @RequestBody Comment comment) {
        postService.addComment(request, postId, comment);
        return R.ok(null);
    }

    @DeleteMapping("/comments/{commentId}")
    public R<?> deleteComment(HttpServletRequest request,
                              @PathVariable Long commentId) {
        postService.deleteComment(request, commentId);
        return R.ok(null);
    }

    @PostMapping("/{postId}/like")
    public R<?> likePost(HttpServletRequest request,
                         @PathVariable Long postId) {
        postService.likePost(request, postId);
        return R.ok(null);
    }

    @PostMapping("/{postId}/favorite")
    public R<?> favoritePost(HttpServletRequest request,
                             @PathVariable Long postId) {
        postService.favoritePost(request, postId);
        return R.ok(null);
    }

}
