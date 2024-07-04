package com.sparta.deventer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Top10ResponseDto {

    private Long id;
    private String username;
    private String nickname;
    private String email;
    private Long followerCount;
}
