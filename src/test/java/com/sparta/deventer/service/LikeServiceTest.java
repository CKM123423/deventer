package com.sparta.deventer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.deventer.dto.CommentResponseDto;
import com.sparta.deventer.dto.PostResponseDto;
import com.sparta.deventer.entity.Like;
import com.sparta.deventer.entity.Post;
import com.sparta.deventer.entity.User;
import com.sparta.deventer.enums.ContentEnumType;
import com.sparta.deventer.repository.LikeCustomRepository;
import com.sparta.deventer.repository.LikeRepository;
import com.sparta.deventer.repository.PostRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @InjectMocks
    private LikeService likeService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private LikeCustomRepository likeCustomRepository;

    @Mock
    private User writerUser;

    @Mock
    private User testUser;

    @Test
    void likeToggle_Like_Test() {
        // Given
        String contentType = "post";
        Long contentId = 1L;
        Post post = new Post("Title", "Content", writerUser, null);
        when(writerUser.getId()).thenReturn(1L);
        when(testUser.getId()).thenReturn(2L);
        when(likeCustomRepository.findLikeByContentAndUser(contentId, ContentEnumType.POST,
                testUser.getId())).thenReturn(null);
        when(postRepository.findById(contentId)).thenReturn(Optional.of(post));

        // When
        String result = likeService.likeToggle(contentType, contentId, testUser);

        // Then
        assertThat(result).isEqualTo("좋아요가 완료 되었습니다.");
        verify(likeRepository, times(1)).save(any(Like.class));
        verify(postRepository, times(1)).findById(contentId);
    }

    @Test
    void likeToggle_UnLike_Test() {
        // Given
        String contentType = "post";
        Long contentId = 1L;
        Post post = new Post("Title", "Content", writerUser, null);
        Like like = new Like(testUser, contentId, ContentEnumType.POST);
        when(writerUser.getId()).thenReturn(1L);
        when(testUser.getId()).thenReturn(2L);
        when(likeCustomRepository.findLikeByContentAndUser(contentId, ContentEnumType.POST,
                testUser.getId())).thenReturn(like);
        when(postRepository.findById(contentId)).thenReturn(Optional.of(post));

        // When
        String result = likeService.likeToggle(contentType, contentId, testUser);

        // Then
        assertThat(result).isEqualTo("좋아요가 취소 되었습니다.");
        verify(likeRepository, times(1)).delete(any(Like.class));
        verify(postRepository, times(1)).findById(contentId);
    }

    @Test
    void getLikedPostsByUser_Test() {
        // Given
        Long userId = testUser.getId();
        Pageable pageable = PageRequest.of(0, 5);
        when(likeCustomRepository.findLikedPostsByUserOrderByCreatedAtDesc(userId, pageable))
                .thenReturn(List.of(new PostResponseDto()));

        // When
        List<PostResponseDto> result = likeService.getLikedPostsByUser(userId, 1);

        // Then
        assertThat(result).isNotEmpty();
        verify(likeCustomRepository, times(1))
                .findLikedPostsByUserOrderByCreatedAtDesc(userId, pageable);
    }

    @Test
    void getLikedCommentByUser_Test() {
        // Given
        Long userId = testUser.getId();
        Pageable pageable = PageRequest.of(0, 5);
        when(likeCustomRepository.findLikedCommentsByUserOrderByCreatedAtDesc(userId, pageable))
                .thenReturn(List.of(new CommentResponseDto()));

        // When
        List<CommentResponseDto> result = likeService.getLikedCommentByUser(userId, 1);

        // Then
        assertThat(result).isNotEmpty();
        verify(likeCustomRepository, times(1))
                .findLikedCommentsByUserOrderByCreatedAtDesc(userId, pageable);
    }
}