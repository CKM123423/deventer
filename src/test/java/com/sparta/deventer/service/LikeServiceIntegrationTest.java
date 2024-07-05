package com.sparta.deventer.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.sparta.deventer.dto.CommentResponseDto;
import com.sparta.deventer.dto.PostResponseDto;
import com.sparta.deventer.entity.User;
import com.sparta.deventer.enums.UserLoginType;
import com.sparta.deventer.enums.UserRole;
import com.sparta.deventer.repository.UserRepository;
import com.sparta.deventer.test.TestDataGenerator;
import java.util.List;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LikeServiceIntegrationTest {

    @Autowired
    private LikeService likeService;

    @Autowired
    private TestDataGenerator testDataGenerator;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    /**
     * 유저 10개, 게시글 10개, 1번 게시글에 댓글 10개 주입
     */
    @BeforeAll
    void setTestDataGenerator() {
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
    @Order(1)
    @DisplayName("Like Success test - Liked")
    void likeToggle_Liked_Test() {
        //Given
        String contentType = "post";
        Long contentId = 1L;

        // When
        String result = likeService.likeToggle(contentType, contentId, testUser);

        // Then
        assertThat(result).isEqualTo("좋아요가 완료 되었습니다.");
    }

    @Test
    @Order(2)
    @DisplayName("Get Liked Posts By User Test - Success")
    void getLikedPostsByUser_Success_Test() {
        // Given
        Long contentId = 1L;

        // When
        List<PostResponseDto> likedPosts = likeService.getLikedPostsByUser(testUser.getId(), 1);

        // Then
        assertThat(likedPosts).isNotEmpty();
        assertThat(likedPosts).hasSize(1);
        assertThat(likedPosts.get(0).getPostId()).isEqualTo(contentId);
    }

    @Test
    @Order(3)
    @DisplayName("Like Success test - Liked")
    void likeToggle_UnLiked_Test() {
        //Given
        String contentType = "post";
        Long contentId = 1L;

        // When
        String result = likeService.likeToggle(contentType, contentId, testUser);

        // Then
        assertThat(result).isEqualTo("좋아요가 취소 되었습니다.");
    }

    @Test
    @Order(4)
    @DisplayName("Get Liked Comments By User Test - Success")
    void getLikedCommentsByUser_Success_Test() {
        // Given
        String contentType = "comment";
        Long contentId = 1L;
        likeService.likeToggle(contentType, contentId, testUser);

        // When
        List<CommentResponseDto> likedComments = likeService.getLikedCommentByUser(testUser.getId(),
                1);

        // Then
        assertThat(likedComments).isNotEmpty();
        assertThat(likedComments).hasSize(1);
        assertThat(likedComments.get(0).getId()).isEqualTo(contentId);
    }
}