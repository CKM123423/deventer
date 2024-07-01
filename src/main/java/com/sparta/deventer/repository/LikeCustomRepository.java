package com.sparta.deventer.repository;

import com.sparta.deventer.dto.CommentResponseDto;
import com.sparta.deventer.dto.PostResponseDto;
import com.sparta.deventer.entity.Like;
import com.sparta.deventer.enums.ContentEnumType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LikeCustomRepository {

    Like findLikeByContentAndUser(Long contentId, ContentEnumType contentType, Long userId);

    Page<PostResponseDto> findLikedPostsByUserOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Page<CommentResponseDto> findLikedCommentsByUserOrderByCreatedAtDesc(Long userId,
            Pageable pageable);
}
