package com.example.instagramapi.service;

import com.example.instagramapi.dto.response.FollowResponse;
import com.example.instagramapi.dto.response.UserResponse;
import com.example.instagramapi.entity.Follow;
import com.example.instagramapi.entity.User;
import com.example.instagramapi.exception.CustomException;
import com.example.instagramapi.exception.ErrorCode;
import com.example.instagramapi.repository.FollowRepository;
import com.example.instagramapi.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    // 팔로워/팔로잉 수 조회
    private FollowResponse getFollowCounts(Long userId, boolean isFollowing) {
        long followerCount = followRepository.countByFollowingId(userId);
        long followingCount = followRepository.countByFollowerId(userId);

        return FollowResponse.of(isFollowing, followerCount, followingCount);
    }

    public FollowResponse follow(String username, Long followerId) {
        // username => 프로필 페이지 주인
        User following = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // followerId => 화살표를 보내는 사람 (로그인한 사람)
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 자기 자신 팔로우 방지
        if (following.getId().equals(follower.getId())) {
            throw new CustomException(ErrorCode.CANNOT_FOLLOW_SELF);
        }

        // 이미 팔로우 중인지 체크(팔로우 상태면 예외 발생)
        if (followRepository.existsByFollowerIdAndFollowingId(follower.getId(), following.getId())) {
            throw new CustomException(ErrorCode.ALREADY_FOLLOWING);
        }

        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();

        followRepository.save(follow);

        return getFollowCounts(following.getId(), true);
    }

    @Transactional
    public FollowResponse unfollow(String username, Long followerId) {
        // 대상 조회
        User following = userRepository.findByUsername(username)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Follow follow = followRepository.
                findByFollowerIdAndFollowingId(followerId, following.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOLLOWING));

        followRepository.delete(follow);

        return getFollowCounts(following.getId(), false);
    }

    // 팔로워 목록
    public List<UserResponse> getFollowers(String username) {
        User foundUser = userRepository.findByUsername(username)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return followRepository.findFollowersByFollowingId(foundUser.getId()).stream()
                .map(follow -> UserResponse.from(follow.getFollower()))
                .toList();
    }

    // 팔로잉 목록
    public List<UserResponse> getFollowings(String username) {
        User foundUser = userRepository.findByUsername(username)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return followRepository.findFollowingByFollowerId(foundUser.getId()).stream()
                .map(follow -> UserResponse.from(follow.getFollowing()))
                .toList();
    }

}
