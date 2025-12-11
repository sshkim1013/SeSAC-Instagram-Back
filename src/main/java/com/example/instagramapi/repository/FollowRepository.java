package com.example.instagramapi.repository;

import com.example.instagramapi.entity.Follow;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    // 팔로우 정보 조회
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    // 팔로우 여부 확인
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    // 팔로워 수(숫자 출력)
    long countByFollowingId(Long followingId);

    // 팔로잉 수(숫자 출력)
    long countByFollowerId(Long followerId);

    // 팔로잉 ID 목록
    @Query("SELECT f.following.id FROM Follow f WHERE f.follower.id = :userId")
    List<Long> findFollowingIdByFollowerId(@Param("userId") Long userId);

    // 팔로워(나를 팔로우 하는 유저들) 목록
    @Query("SELECT f FROM Follow f JOIN FETCH f.follower WHERE f.following.id = :userId")
    List<Follow> findFollowersByFollowingId(@Param("userId") Long userId);

    // 팔로잉(내가 팔로우 하는 유저들) 목록
    @Query("SELECT f FROM Follow f JOIN FETCH f.following WHERE f.follower.id = :userId")
    List<Follow> findFollowingByFollowerId(@Param("userId") Long userId);

}
