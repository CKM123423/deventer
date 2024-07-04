package com.sparta.deventer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sparta.deventer.dto.FollowRequestDto;
import com.sparta.deventer.dto.FollowResponseDto;
import com.sparta.deventer.entity.User;
import com.sparta.deventer.enums.FollowError;
import com.sparta.deventer.enums.MismatchStatusEntity;
import com.sparta.deventer.enums.UserLoginType;
import com.sparta.deventer.enums.UserRole;
import com.sparta.deventer.exception.MismatchStatusException;
import com.sparta.deventer.repository.UserRepository;
import com.sparta.deventer.test.TestDataGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FollowServiceIntegrationTest {

    @Autowired
    private FollowService followService;

    @Autowired
    private TestDataGenerator testDataGenerator;

    @Autowired
    private UserRepository userRepository;

    private User testUser1;
    private User testUser2;

    @BeforeAll
    void setTestDataGenerator() {
        testDataGenerator.generateTestData();

        testUser1 = new User(
                "testUsername1",
                "password",
                "testNickname1",
                UserRole.USER,
                "user1@email.com",
                UserLoginType.DEFAULT
        );

        testUser2 = new User(
                "testUsername2",
                "password",
                "testNickname2",
                UserRole.USER,
                "user2@email.com",
                UserLoginType.DEFAULT
        );

        userRepository.save(testUser1);
        userRepository.save(testUser2);
    }

    @Test
    @Order(1)
    @DisplayName("Unfollow User - Not Following")
    @Transactional
    void unfollowUser_NotFollowing_Test() {
        // Given
        FollowRequestDto requestDto = new FollowRequestDto(testUser2.getNickname());

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            followService.unFollowUser(requestDto, testUser1);
        });

        // Then
        assertThat(exception.getMessage()).isEqualTo(FollowError.NOT_FOLLOWING.getMessage());
    }

    @Test
    @Order(2)
    @DisplayName("Follow User - Success")
    void followUser_Success_Test() {
        // Given
        FollowRequestDto requestDto = new FollowRequestDto(testUser2.getNickname());

        // When
        FollowResponseDto responseDto = followService.followUser(requestDto, testUser1);

        // Then
        assertThat(responseDto.getFollowerUserNickname()).isEqualTo(testUser1.getNickname());
        assertThat(responseDto.getFollowingUserNickname()).isEqualTo(testUser2.getNickname());
    }

    @Test
    @Order(3)
    @DisplayName("Follow User - Already Following")
    void followUser_AlreadyFollowing_Test() {
        // Given
        FollowRequestDto requestDto = new FollowRequestDto(testUser2.getNickname());

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            followService.followUser(requestDto, testUser1);
        });

        // Then
        assertThat(exception.getMessage()).isEqualTo(FollowError.ALREADY_FOLLOWING.getMessage());
    }

    @Test
    @Order(4)
    @DisplayName("Follow User - Following Self")
    void followUser_FollowingSelf_Test() {
        // Given
        FollowRequestDto requestDto = new FollowRequestDto(testUser1.getNickname());

        // When
        MismatchStatusException exception = assertThrows(MismatchStatusException.class, () -> {
            followService.followUser(requestDto, testUser1);
        });

        // Then
        assertThat(exception.getMessage()).isEqualTo(MismatchStatusEntity.SELF_USER.getMessage());
    }

    @Test
    @Order(5)
    @DisplayName("Unfollow User - Success")
    @Transactional
    void unfollowUser_Success_Test() {
        // Given
        FollowRequestDto requestDto = new FollowRequestDto(testUser2.getNickname());

        // When
        String result = followService.unFollowUser(requestDto, testUser1);

        // Then
        assertThat(result).isEqualTo(FollowError.FOLLOW_CANCELLED.getMessage());
    }
}