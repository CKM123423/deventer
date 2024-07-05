package com.sparta.deventer.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.deventer.dto.Top10ResponseDto;
import com.sparta.deventer.entity.QFollow;
import com.sparta.deventer.entity.QUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FollowCustomRepository {

    private final JPAQueryFactory queryFactory;

    public List<Top10ResponseDto> getTop10UsersByFollowerCount() {

        QFollow follow = QFollow.follow;
        QUser user = QUser.user;

        return queryFactory
                .select(Projections.constructor(
                        Top10ResponseDto.class,
                        user.id,
                        user.username,
                        user.nickname,
                        user.email,
                        follow.follower.count()
                ))
                .from(user)
                .join(follow).on(follow.follower.id.eq(user.id))
                .groupBy(user.id)
                .orderBy(follow.follower.count().desc())
                .limit(10)
                .fetch();
    }
}
