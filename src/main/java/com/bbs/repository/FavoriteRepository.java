package com.bbs.repository;

import com.bbs.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Favorite findByUserIdAndPostId(Long userId, Long postId);

    void deleteByPostId(Long postId);
}
