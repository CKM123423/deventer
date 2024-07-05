package com.sparta.deventer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LikeRequestDto {

    @NotNull(message = "컨텐츠 아이디는 필수 입력값 입니다.")
    private Long contentId;

    @NotBlank(message = "컨텐츠 타입은 필수 입력값 입니다.")
    private String contentType;
}
