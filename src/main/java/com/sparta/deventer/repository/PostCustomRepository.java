package com.sparta.deventer.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.deventer.dto.PostResponseDto;
import com.sparta.deventer.dto.PostSearchCond;
import com.sparta.deventer.entity.QFollow;
import com.sparta.deventer.entity.QPost;
import com.sparta.deventer.entity.QUser;
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
            Pageable pageable, String sortBy, PostSearchCond postSearchCond) {
        QFollow follow = QFollow.follow;
        QPost post = QPost.post;
        QUser user = QUser.user;

        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sortBy, post);

        BooleanBuilder builder = buildSearchFilter(user, postSearchCond);

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
                .where(follow.follower.id.eq(userId)
                        .and(builder))
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private OrderSpecifier<?> getOrderSpecifier(String sortBy, QPost post) {
        if ("author".equalsIgnoreCase(sortBy)) {
            return post.user.nickname.asc();
        } else {
            return post.createdAt.desc();
        }
    }

    private BooleanBuilder buildSearchFilter(QUser user, PostSearchCond postSearchCond) {

        BooleanBuilder builder = new BooleanBuilder();

        if (postSearchCond.getUsername() != null) {
            builder.and(user.username.eq(postSearchCond.getUsername()));
        }

        if (postSearchCond.getEmail() != null) {
            builder.and(user.email.eq(postSearchCond.getEmail()));
        }

        if (postSearchCond.getNickname() != null) {
            builder.and(user.nickname.eq(postSearchCond.getNickname()));
        }
        
        return builder;
    }
}
