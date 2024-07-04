package com.sparta.deventer.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FollowResponseDto {

    private String followerUserNickname;

    private String followingUserNickname;

    private LocalDateTime createdAt;


    public static FollowResponseDto of(String followerUserNickname, String followingUserNickname,
            LocalDateTime createdAt) {
        return FollowResponseDto.builder()
                .followerUserNickname(followerUserNickname)
                .followingUserNickname(followingUserNickname)
                .createdAt(createdAt)
                .build();
    }
}
