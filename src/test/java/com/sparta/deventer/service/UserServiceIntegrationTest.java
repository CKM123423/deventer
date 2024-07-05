package com.sparta.deventer.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.sparta.deventer.dto.ProfileResponseDto;
import com.sparta.deventer.entity.Like;
import com.sparta.deventer.entity.User;
import com.sparta.deventer.enums.ContentEnumType;
import com.sparta.deventer.enums.UserLoginType;
import com.sparta.deventer.enums.UserRole;
import com.sparta.deventer.repository.LikeRepository;
import com.sparta.deventer.repository.UserRepository;
import com.sparta.deventer.test.TestDataGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private TestDataGenerator testDataGenerator;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeRepository likeRepository;

    private User testUser;

    @BeforeAll
    void setUp() {
        testDataGenerator.generateTestData();

        testUser = new User(
                "testUsername",
                "password",
                "testNickname",
                UserRole.USER,
                "user2@email.com",
                UserLoginType.DEFAULT
        );

        userRepository.save(testUser);
    }

    @Test
    @DisplayName("Get Profile - Success")
    void getProfile_Success_Test() {
        // Given
        Long postContentId = 1L;
        Long commentContentId = 1L;

        Like likePosts = Like.builder()
                .user(testUser)
                .contentType(ContentEnumType.POST)
                .typeId(postContentId)
                .build();

        Like likeComments = Like.builder()
                .user(testUser)
                .contentType(ContentEnumType.COMMENT)
                .typeId(commentContentId)
                .build();

        likeRepository.save(likePosts);
        likeRepository.save(likeComments);

        // When
        ProfileResponseDto profileResponseDto = userService.getProfile(testUser.getId(), testUser);

        // Then
        assertThat(profileResponseDto).isNotNull();
        assertThat(profileResponseDto.getPostsLikedCount()).isEqualTo(1L);
        assertThat(profileResponseDto.getCommentsLikedCount()).isEqualTo(1L);
    }
}