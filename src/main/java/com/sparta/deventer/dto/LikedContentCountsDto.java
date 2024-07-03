package com.sparta.deventer.dto;

import com.sparta.deventer.enums.ContentEnumType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LikedContentCountsDto {

    private ContentEnumType contentType;
    private Long count;
}
