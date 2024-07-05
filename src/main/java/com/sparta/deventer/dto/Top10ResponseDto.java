package com.sparta.deventer.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Top10ResponseDto {

    private Long id;
    private String username;
    private String nickname;
    private String email;
    private Long followerCount;


    @QueryProjection
    public Top10ResponseDto(Long id, String username, String nickname, String email,
            Long followerCount) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.followerCount = followerCount;
    }
}
