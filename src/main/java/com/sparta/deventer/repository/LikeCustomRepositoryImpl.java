package com.sparta.deventer.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.deventer.dto.PostResponseDto;
import com.sparta.deventer.entity.Like;
import com.sparta.deventer.entity.Post;
import com.sparta.deventer.entity.QLike;
import com.sparta.deventer.entity.QPost;
import com.sparta.deventer.enums.ContentEnumType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

    /*
     * SELECT p.*
     * FROM likes l
     * JOIN posts p ON l.content_id = p.id
     * WHERE l.user_id = ?
     * ORDER BY p.created_at DESC
     * LIMIT ? OFFSET ?
     */
    @Override
    public Page<PostResponseDto> findLikedPostsByUserOrderByCreatedAtDesc(Long userId,
            Pageable pageable) {
        QLike qLike = QLike.like;
        QPost qPost = QPost.post;

        List<Post> posts = queryFactory
                .select(qPost)
                .from(qLike)
                .join(qPost).on(qLike.contentId.eq(qPost.id))
                .where(qLike.user.id.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qPost.createdAt.desc())
                .fetch();

        Long totalCount = queryFactory
                .select(qLike.count())
                .from(qLike)
                .where(qLike.user.id.eq(userId))
                .fetchOne();

        if (totalCount == null) {
            totalCount = 0L;
        }

        List<PostResponseDto> results = posts.stream()
                .map(PostResponseDto::new)
                .toList();

        return new PageImpl<>(results, pageable, totalCount);
    }
}
