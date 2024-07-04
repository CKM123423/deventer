package com.sparta.deventer.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.deventer.dto.PostResponseDto;
import com.sparta.deventer.entity.QFollow;
import com.sparta.deventer.entity.QPost;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PostCustomRepository {

    private final JPAQueryFactory queryFactory;

    public List<PostResponseDto> findPostsByFollowedUsersWithSorting(Long userId,
            Pageable pageable, String sortBy) {
        QFollow follow = QFollow.follow;
        QPost post = QPost.post;

        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sortBy, post);

        return queryFactory
                .select(Projections.constructor(
                        PostResponseDto.class,
                        post.id,
                        post.user.nickname,
                        post.category.topic,
                        post.title,
                        post.content,
                        post.likeCount,
                        post.createdAt,
                        post.updatedAt
                ))
                .from(post)
                .join(follow).on(post.user.id.eq(follow.following.id))
                .where(follow.follower.id.eq(userId))
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private OrderSpecifier<?> getOrderSpecifier(String sortBy, QPost post) {
        if ("writer".equalsIgnoreCase(sortBy)) {
            return post.user.nickname.asc();
        } else {
            return post.createdAt.desc();
        }
    }
}
