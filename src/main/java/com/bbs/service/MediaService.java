package com.bbs.service;

import com.bbs.entity.Post;
import com.bbs.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Service
public class MediaService {
    @Resource
    private PostRepository postRepository;

    // 保存文件并返回文件的URL
    public String saveFile(MultipartFile file) throws IOException {
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String uploadDir = "D:\\ZhuoMian\\MyGame\\final\\files";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(filename);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            return filePath.toString();
        } catch (IOException e) {
            throw new IOException("Could not save file: " + filename, e);
        }
    }

    // 更新帖子的媒体列表
    public void addMediaToPost(Long postId, List<String> mediaUrls) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        List<String> images = post.getImages();
        List<String> videos = post.getVideos();

        for (String url : mediaUrls) {
            if (url.endsWith(".jpg") || url.endsWith(".jpeg") || url.endsWith(".png")) {
                images.add(url);
            } else if (url.endsWith(".mp4")) {
                videos.add(url);
            }
        }

        post.setImages(images);
        post.setVideos(videos);
        postRepository.save(post);
    }
}
