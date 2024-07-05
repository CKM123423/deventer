package com.sparta.deventer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostSearchCond {

    private String username;
    private String email;
    private String nickname;
}