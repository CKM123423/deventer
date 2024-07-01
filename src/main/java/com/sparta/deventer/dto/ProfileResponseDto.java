package com.sparta.deventer.dto;

import com.sparta.deventer.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProfileResponseDto {

    private Long userId;
    private String username;
    private String nickname;
    private String email;
    private long postsLikedCount;
    private long commentsLikedCount;

    @Builder
    public ProfileResponseDto(User user, long postsLikedCount, long commentsLikedCount) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.postsLikedCount = postsLikedCount;
        this.commentsLikedCount = commentsLikedCount;
    }
}