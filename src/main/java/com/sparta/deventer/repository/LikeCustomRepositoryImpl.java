package com.sparta.deventer.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.deventer.entity.Comment;
import com.sparta.deventer.entity.Like;
import com.sparta.deventer.entity.Post;
import com.sparta.deventer.entity.QComment;
import com.sparta.deventer.entity.QLike;
import com.sparta.deventer.entity.QPost;
import com.sparta.deventer.enums.ContentEnumType;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LikeCustomRepositoryImpl implements LikeCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Like findLikeByContentAndUser(Long contentId, ContentEnumType contentType, Long userId) {
        QLike qLike = QLike.like;
        return queryFactory.selectFrom(qLike)
                .where(qLike.contentId.eq(contentId)
                        .and(qLike.contentType.eq(contentType))
                        .and(qLike.user.id.eq(userId)))
                .fetchOne();
    }

    @Override
    public List<Post> findLikedPostsByUserOrderByCreatedAtDesc(Long userId,
            Pageable pageable) {
        QLike qLike = QLike.like;
        QPost qPost = QPost.post;

        return queryFactory
                .select(qPost)
                .from(qLike)
                .join(qPost).on(qLike.contentId.eq(qPost.id))
                .where(qLike.user.id.eq(userId)
                        .and(qLike.contentType.eq(ContentEnumType.POST)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qPost.createdAt.desc())
                .fetch();
    }

    @Override
    public List<Comment> findLikedCommentsByUserOrderByCreatedAtDesc(Long userId,
            Pageable pageable) {
        QLike qLike = QLike.like;
        QComment qComment = QComment.comment;

        return queryFactory
                .select(qComment)
                .from(qLike)
                .join(qComment).on(qLike.contentId.eq(qComment.id))
                .where(qLike.user.id.eq(userId)
                        .and(qLike.contentType.eq(ContentEnumType.COMMENT)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qComment.createdAt.desc())
                .fetch();
    }

    @Override
    public long countLikedContentByUserId(Long userId, ContentEnumType contentType) {
        QLike qLike = QLike.like;

        return Optional.ofNullable(queryFactory
                .select(qLike.count())
                .from(qLike)
                .where(qLike.user.id.eq(userId)
                        .and(qLike.contentType.eq(contentType)))
                .fetchOne()).orElse(0L);
    }
}
