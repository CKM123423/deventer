package com.sparta.deventer.service;

import com.sparta.deventer.dto.FollowRequestDto;
import com.sparta.deventer.dto.FollowResponseDto;
import com.sparta.deventer.entity.Follow;
import com.sparta.deventer.entity.User;
import com.sparta.deventer.enums.FollowError;
import com.sparta.deventer.enums.MismatchStatusEntity;
import com.sparta.deventer.enums.NotFoundEntity;
import com.sparta.deventer.exception.EntityNotFoundException;
import com.sparta.deventer.exception.MismatchStatusException;
import com.sparta.deventer.repository.FollowRepository;
import com.sparta.deventer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public FollowResponseDto followUser(FollowRequestDto requestDto, User followerUser) {
        if (followerUser.isSameUserNickname(requestDto.getFollowingUserNickname())) {
            throw new MismatchStatusException(MismatchStatusEntity.SELF_USER);
        }

        User followingUser = userRepository.findByNickname(requestDto.getFollowingUserNickname())
                .orElseThrow(() -> new EntityNotFoundException(NotFoundEntity.USER_NOT_FOUND));

        followRepository.findByFollowerAndFollowing(followerUser, followingUser)
                .ifPresent(follow -> {
                    throw new IllegalArgumentException(FollowError.ALREADY_FOLLOWING.getMessage());
                });

        Follow follow = Follow.builder()
                .follower(followerUser)
                .following(followingUser)
                .build();

        followRepository.save(follow);

        return FollowResponseDto.of(
                follow.getFollower().getNickname(),
                follow.getFollowing().getNickname(),
                follow.getCreatedAt());
    }

    @Transactional
    public String unFollowUser(FollowRequestDto requestDto, User followerUser) {
        if (followerUser.isSameUserNickname(requestDto.getFollowingUserNickname())) {
            throw new MismatchStatusException(MismatchStatusEntity.SELF_USER);
        }

        User followingUser = userRepository.findByNickname(requestDto.getFollowingUserNickname())
                .orElseThrow(() -> new EntityNotFoundException(NotFoundEntity.USER_NOT_FOUND));

        Follow follow = followRepository.findByFollowerAndFollowing(followerUser, followingUser)
                .orElseThrow(
                        () -> new IllegalArgumentException(FollowError.NOT_FOLLOWING.getMessage()));

        followRepository.delete(follow);

        return FollowError.FOLLOW_CANCELLED.getMessage();
    }
}
