package com.sparta.deventer.service;

import com.sparta.deventer.dto.CommentResponseDto;
import com.sparta.deventer.dto.PostResponseDto;
import com.sparta.deventer.entity.Comment;
import com.sparta.deventer.entity.Like;
import com.sparta.deventer.entity.Post;
import com.sparta.deventer.entity.User;
import com.sparta.deventer.enums.ContentEnumType;
import com.sparta.deventer.enums.MismatchStatusEntity;
import com.sparta.deventer.enums.NotFoundEntity;
import com.sparta.deventer.exception.EntityNotFoundException;
import com.sparta.deventer.exception.MismatchStatusException;
import com.sparta.deventer.repository.CommentRepository;
import com.sparta.deventer.repository.LikeCustomRepository;
import com.sparta.deventer.repository.LikeRepository;
import com.sparta.deventer.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final LikeCustomRepository likeCustomRepository;

    @Transactional
    public String likeToggle(String contentType, Long contentId, User user) {

        ContentEnumType type = ContentEnumType.getByType(contentType);

        Like existingLike = likeCustomRepository.findLikeByContentAndUser(contentId, type,
                user.getId());

        if (existingLike == null) {
            Object content = validateAndGetContent(contentType, contentId, user);
            Like saveLike = new Like(user, contentId, type);
            likeRepository.save(saveLike);

            if (content instanceof Post) {
                ((Post) content).likeCountUp();
            } else if (content instanceof Comment) {
                ((Comment) content).likeCountUp();
            }

            return "좋아요가 완료 되었습니다.";
        } else {
            Object content = validateAndGetContent(contentType, contentId, user);

            if (content instanceof Post) {
                ((Post) content).likeCountDown();
            } else if (content instanceof Comment) {
                ((Comment) content).likeCountDown();
            }

            likeRepository.delete(existingLike);

            return "좋아요가 취소 되었습니다.";
        }
    }

    @Transactional(readOnly = true)
    public Slice<PostResponseDto> getLikedPostsByUser(Long userId, int page) {
        Pageable pageable = PageRequest.of(page - 1, 5);
        return likeCustomRepository.findLikedPostsByUserOrderByCreatedAtDesc(userId, pageable);
    }

    @Transactional(readOnly = true)
    public Slice<CommentResponseDto> getLikedCommentByUser(Long userId, int page) {
        Pageable pageable = PageRequest.of(page - 1, 5);
        return likeCustomRepository.findLikedCommentsByUserOrderByCreatedAtDesc(userId, pageable);
    }

    private Object validateAndGetContent(String contentType, Long contentId, User user) {
        if (contentType.equals(ContentEnumType.POST.getType())) {
            Post post = postRepository.findById(contentId)
                    .orElseThrow(() -> new EntityNotFoundException(NotFoundEntity.POST_NOT_FOUND));

            if (user.isSameUserId(post.getUser().getId())) {
                throw new MismatchStatusException(MismatchStatusEntity.SELF_USER);
            }

            return post;
        } else if (contentType.equals(ContentEnumType.COMMENT.getType())) {
            Comment comment = commentRepository.findById(contentId)
                    .orElseThrow(
                            () -> new EntityNotFoundException(NotFoundEntity.COMMENT_NOT_FOUND));

            if (user.isSameUserId(comment.getUser().getId())) {
                throw new MismatchStatusException(MismatchStatusEntity.SELF_USER);
            }

            return comment;
        }
        throw new IllegalArgumentException("지원하지 않는 타입입니다.");
    }
}