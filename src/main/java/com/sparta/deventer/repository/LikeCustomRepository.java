package com.sparta.deventer.repository;

import com.sparta.deventer.entity.Comment;
import com.sparta.deventer.entity.Like;
import com.sparta.deventer.entity.Post;
import com.sparta.deventer.enums.ContentEnumType;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface LikeCustomRepository {

    Like findLikeByContentAndUser(Long contentId, ContentEnumType contentType, Long userId);

    List<Post> findLikedPostsByUserOrderByCreatedAtDesc(Long userId, Pageable pageable);

    List<Comment> findLikedCommentsByUserOrderByCreatedAtDesc(Long userId,
            Pageable pageable);

    long countLikedContentByUserId(Long userId, ContentEnumType contentType);
}
