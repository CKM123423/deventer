package com.sparta.deventer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FollowError {

    ALREADY_FOLLOWING("이미 팔로우한 유저입니다."),
    NOT_FOLLOWING("팔로우 내역이 존재하지 않습니다."),
    FOLLOW_CANCELLED("팔로우가 취소되었습니다.");

    private final String message;
}
