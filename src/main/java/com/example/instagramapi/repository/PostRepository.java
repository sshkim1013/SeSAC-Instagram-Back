package com.example.instagramapi.repository;

import com.example.instagramapi.entity.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    // N+1 문제 발생 방지를 위해 @Query 사용

    // 전체 게시물 조회
    @Query("SELECT p "
         + "FROM Post p JOIN FETCH p.user "
         + "ORDER BY p.createdAt DESC")
    List<Post> findAllWithUser();

    // 단일 게시물 조회
    @Query("SELECT p "
         + "FROM Post p JOIN FETCH p.user "
         + "WHERE p.id = :id")
    Optional<Post> findByIdWithUser(@Param("id") Long id);

    // 특정 사용자의 게시물 조회
    @Query("SELECT p "
         + "FROM Post p JOIN FETCH p.user "
         + "WHERE p.user.id = :userId "
         + "ORDER BY p.createdAt DESC")
    List<Post> findByUserIdWithUser(@Param("userId") Long userId);

    // 사용자 별 게시물 수
    long countByUserId(Long userId);

    // 탐색
    @Query("SELECT p FROM Post p JOIN FETCH p.user ORDER BY p.createdAt DESC")
    Slice<Post> findAllWithUserPaging(Pageable pageable);

    // 피드(내 게시물 + 팔로잉 게시물)
    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.user.id IN :userIds ORDER BY p.createdAt DESC")
    Slice<Post> findByUserIdsWithUserPaging(@Param("userIds") List<Long> userIds, Pageable pageable);

}
