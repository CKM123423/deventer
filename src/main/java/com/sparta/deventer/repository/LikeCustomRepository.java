package com.sparta.deventer.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.deventer.dto.CommentResponseDto;
import com.sparta.deventer.dto.LikedContentCountsDto;
import com.sparta.deventer.dto.PostResponseDto;
import com.sparta.deventer.entity.Like;
import com.sparta.deventer.entity.QComment;
import com.sparta.deventer.entity.QLike;
import com.sparta.deventer.entity.QPost;
import com.sparta.deventer.enums.ContentEnumType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LikeCustomRepository {

    private final JPAQueryFactory queryFactory;

    public Like findLikeByContentAndUser(Long contentId, ContentEnumType contentType, Long userId) {
        QLike qLike = QLike.like;

        return queryFactory.selectFrom(qLike)
                .where(qLike.contentId.eq(contentId)
                        .and(qLike.contentType.eq(contentType))
                        .and(qLike.user.id.eq(userId)))
                .fetchOne();
    }

    public Slice<PostResponseDto> findLikedPostsByUserOrderByCreatedAtDesc(Long userId,
            Pageable pageable) {
        QLike qLike = QLike.like;
        QPost qPost = QPost.post;

        List<PostResponseDto> responseDtoList = queryFactory
                .select(Projections.constructor(
                        PostResponseDto.class,
                        qPost.id,
                        qPost.user.nickname,
                        qPost.category.topic,
                        qPost.title,
                        qPost.content,
                        qPost.likeCount,
                        qPost.createdAt,
                        qPost.updatedAt
                ))
                .from(qLike)
                .join(qPost).on(qLike.contentId.eq(qPost.id))
                .where(qLike.user.id.eq(userId)
                        .and(qLike.contentType.eq(ContentEnumType.POST)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(qPost.createdAt.desc())
                .fetch();

        boolean hasNext = false;
        if (responseDtoList.size() > pageable.getPageSize()) {
            responseDtoList.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(responseDtoList, pageable, hasNext);
    }

    public Slice<CommentResponseDto> findLikedCommentsByUserOrderByCreatedAtDesc(Long userId,
            Pageable pageable) {
        QLike qLike = QLike.like;
        QComment qComment = QComment.comment;

        List<CommentResponseDto> responseDtoList = queryFactory
                .select(Projections.constructor(
                        CommentResponseDto.class,
                        qComment.content,
                        qComment.id,
                        qComment.user.id,
                        qComment.user.nickname,
                        qComment.likeCount,
                        qComment.createdAt,
                        qComment.updatedAt
                ))
                .from(qLike)
                .join(qComment).on(qLike.contentId.eq(qComment.id))
                .where(qLike.user.id.eq(userId)
                        .and(qLike.contentType.eq(ContentEnumType.COMMENT)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(qComment.createdAt.desc())
                .fetch();

        boolean hasNext = false;
        if (responseDtoList.size() > pageable.getPageSize()) {
            responseDtoList.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(responseDtoList, pageable, hasNext);
    }

    public List<LikedContentCountsDto> likedCountsGroupByContentType(Long userId) {
        QLike qLike = QLike.like;

        return queryFactory
                .select(Projections.constructor(
                        LikedContentCountsDto.class,
                        qLike.contentType,
                        qLike.count()
                ))
                .from(qLike)
                .where(qLike.user.id.eq(userId))
                .groupBy(qLike.contentType)
                .fetch();
    }
}
