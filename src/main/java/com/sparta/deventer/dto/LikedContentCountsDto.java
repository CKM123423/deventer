package com.sparta.deventer.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.deventer.enums.ContentEnumType;
import lombok.Getter;

@Getter
public class LikedContentCountsDto {

    private ContentEnumType contentType;
    private Long count;

    @QueryProjection
    public LikedContentCountsDto(ContentEnumType contentType, Long count) {
        this.contentType = contentType;
        this.count = count;
    }
}
