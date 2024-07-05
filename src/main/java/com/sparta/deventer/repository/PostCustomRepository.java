package com.sparta.deventer.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PostCustomRepository {

    private final JPAQueryFactory queryFactory;

    public Slice<PostResponseDto> findPostsByFollowedUsersWithSorting(Long userId,
            Pageable pageable, String sortBy, PostSearchCond postSearchCond) {
        QFollow follow = QFollow.follow;
        QPost post = QPost.post;

        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sortBy, post);

        List<PostResponseDto> responseDtoList = queryFactory
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
                .where(
                        follow.follower.id.eq(userId),
                        usernameEq(postSearchCond.getUsername()),
                        emailEq(postSearchCond.getEmail()),
                        nicknameEq(postSearchCond.getNickname())
                )
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        if (responseDtoList.size() > pageable.getPageSize()) {
            responseDtoList.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(responseDtoList, pageable, hasNext);
    }

    private OrderSpecifier<?> getOrderSpecifier(String sortBy, QPost post) {
        if ("author".equalsIgnoreCase(sortBy)) {
            return post.user.nickname.asc();
        } else {
            return post.createdAt.desc();
        }
    }

    private BooleanExpression usernameEq(String username) {
        return StringUtils.hasText(username) ? QUser.user.username.eq(username) : null;
    }

    private BooleanExpression emailEq(String email) {
        return StringUtils.hasText(email) ? QUser.user.email.eq(email) : null;
    }

    private BooleanExpression nicknameEq(String nickname) {
        return StringUtils.hasText(nickname) ? QUser.user.nickname.eq(nickname) : null;
    }
}
