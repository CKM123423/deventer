package com.sparta.deventer.repository;

import com.sparta.deventer.entity.Follow;
import com.sparta.deventer.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFollowerAndFollowing(User followerUser, User followingUser);

}
