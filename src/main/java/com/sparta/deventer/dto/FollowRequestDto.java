package com.sparta.deventer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FollowRequestDto {

    @NotBlank(message = "팔로우 대상 닉네임은 필수 입력값입니다.")
    private String followingUserNickname;
}
