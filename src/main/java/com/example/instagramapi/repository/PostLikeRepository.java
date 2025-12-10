package com.example.instagramapi.repository;

import com.example.instagramapi.entity.PostLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    // 좋아요 조회
    Optional<PostLike> findByUserIdAndPostId(Long userId, Long postId);

    // 좋아요 여부 확인
    boolean existsByUserIdAndPostId(Long userId, Long postId);

    // 게시물의 좋아요 수
    long countByPostId(Long postId);

}
