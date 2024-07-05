package com.sparta.deventer.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.sparta.deventer.dto.FollowRequestDto;
import com.sparta.deventer.dto.PostResponseDto;
import com.sparta.deventer.dto.PostSearchCond;
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
class PostServiceIntegrationTest {

    @Autowired
    private PostService postService;

    @Autowired
    private TestDataGenerator testDataGenerator;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowService followService;

    private User testUser1;

    @BeforeAll
    void setTestDataGenerator_Success_Test() {
        testDataGenerator.generateTestData();

        testUser1 = new User(
                "testUsername1",
                "password",
                "testNickname1",
                UserRole.USER,
                "user1@email.com",
                UserLoginType.DEFAULT
        );

        userRepository.save(testUser1);
    }

    @Test
    @Order(1)
    @DisplayName("Get Posts By Following User Sort By Created At - Success")
    void getPostsByFollowingUser_Success_Test() {
        // Given
        int page = 1;
        String sortBy = "createdAt";
        PostSearchCond postSearchCond = new PostSearchCond();
        followService.followUser(new FollowRequestDto("nickname"), testUser1);

        // When
        List<PostResponseDto> posts = postService.getPostsByFollowingUser(page, testUser1, sortBy,
                postSearchCond);

        // Then
        assertThat(posts).isNotNull();
        assertThat(posts.size()).isEqualTo(5);
        assertThat(posts.get(0).getTitle()).isEqualTo("Title 10");
        assertThat(posts.get(1).getTitle()).isEqualTo("Title 9");
        assertThat(posts.get(2).getTitle()).isEqualTo("Title 8");
        assertThat(posts.get(3).getTitle()).isEqualTo("Title 7");
        assertThat(posts.get(4).getTitle()).isEqualTo("Title 6");
    }

    @Test
    @Order(2)
    @DisplayName("Get Posts By Following User Sort By Created At And Filter Username - Success")
    void getPostsByFollowingUser_Filter_Username_Success_Test() {
        // Given
        int page = 1;
        String sortBy = "createdAt";
        String username = "username";
        PostSearchCond postSearchCond = new PostSearchCond(username, null, null);

        // When
        List<PostResponseDto> posts = postService.getPostsByFollowingUser(page, testUser1, sortBy,
                postSearchCond);

        // Then
        assertThat(posts).isNotNull();
        assertThat(posts.size()).isEqualTo(5);
    }

    @Test
    @Order(3)
    @DisplayName("Get Posts By Following User Sort By Writer - Success")
    void getPostsByFollowingUser_SortByWriter_Success_Test() {
        // Given
        int page = 1;
        String sortBy = "writer";
        PostSearchCond postSearchCond = new PostSearchCond();

        // When
        List<PostResponseDto> posts = postService.getPostsByFollowingUser(page, testUser1, sortBy,
                postSearchCond);

        // Then
        assertThat(posts).isNotNull();
        assertThat(posts.size()).isEqualTo(5);
        assertThat(posts.get(0).getNickname()).isEqualTo("nickname");
    }
}