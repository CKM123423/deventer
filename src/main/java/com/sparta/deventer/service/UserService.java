package com.sparta.deventer.service;

import com.sparta.deventer.dto.ChangePasswordRequestDto;
import com.sparta.deventer.dto.CommentResponseDto;
import com.sparta.deventer.dto.PostResponseDto;
import com.sparta.deventer.dto.ProfileResponseDto;
import com.sparta.deventer.dto.UpdateProfileRequestDto;
import com.sparta.deventer.entity.PasswordHistory;
import com.sparta.deventer.entity.User;
import com.sparta.deventer.enums.ContentEnumType;
import com.sparta.deventer.exception.InvalidPasswordException;
import com.sparta.deventer.exception.InvalidUserException;
import com.sparta.deventer.repository.CommentRepository;
import com.sparta.deventer.repository.LikeRepository;
import com.sparta.deventer.repository.PasswordHistoryRepository;
import com.sparta.deventer.repository.PostRepository;
import com.sparta.deventer.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final LikeRepository likeRepository;

    public ProfileResponseDto getProfile(Long userId, User user) {
        if (!user.checkUserId(userId)) {
            throw new InvalidUserException("본인만 사용가능한 기능입니다.");
        }
        long postsLikedCount = likeRepository.countLikedContentByUserId(userId,
                ContentEnumType.POST);
        long commentsLikedCount = likeRepository.countLikedContentByUserId(userId,
                ContentEnumType.COMMENT);
        return ProfileResponseDto.builder()
                .user(user)
                .postsLikedCount(postsLikedCount)
                .commentsLikedCount(commentsLikedCount)
                .build();
    }

    public Page<PostResponseDto> getAllPosts(Long userId, User user, Pageable pageable) {
        if (!user.checkUserId(userId)) {
            throw new InvalidUserException("본인만 사용가능한 기능입니다.");
        }
        return postRepository.findByUserId(userId, pageable).map(PostResponseDto::new);
    }

    public Page<CommentResponseDto> getAllComments(Long userId, User user, Pageable pageable) {
        if (!user.checkUserId(userId)) {
            throw new InvalidUserException("본인만 사용가능한 기능입니다.");
        }
        return commentRepository.findByUserId(userId, pageable).map(CommentResponseDto::new);
    }

    public ProfileResponseDto updateProfile(Long userId,
            UpdateProfileRequestDto updateProfileRequestDto, User user) {

        if (!user.checkUserId(userId)) {
            throw new InvalidUserException("본인만 사용가능한 기능입니다.");
        }

        user.updateUserProfile(updateProfileRequestDto.getNickname(),
                updateProfileRequestDto.getEmail());

        userRepository.save(user);
        return ProfileResponseDto.builder()
                .user(user)
                .build();
    }

    public void changePassword(Long userId, ChangePasswordRequestDto changePasswordRequestDto,
            User user) {

        if (!user.checkUserId(userId)) {
            throw new InvalidUserException("본인만 사용가능한 기능입니다.");
        }

        String currentPassword = changePasswordRequestDto.getCurrentPassword();
        String newPassword = changePasswordRequestDto.getNewPassword();
        List<PasswordHistory> passwordHistoryList =
                passwordHistoryRepository.findByUserOrderByCreatedAtAsc(user);

        user.validatePassword(passwordEncoder, currentPassword);
        validateNewPassword(user, newPassword);
        validatePasswordHistory(newPassword, passwordHistoryList);

        PasswordHistory newHistory = new PasswordHistory(passwordEncoder.encode(newPassword), user);
        passwordHistoryRepository.save(newHistory);

        if (passwordHistoryList.size() > 2) {
            passwordHistoryRepository.delete(passwordHistoryList.get(0));
        }

        user.updatePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private void validateNewPassword(User user, String newPassword) {
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new InvalidPasswordException("새 비밀번호는 현재 비밀번호와 달라야 합니다.");
        }
    }

    public void validatePasswordHistory(String newPassword,
            List<PasswordHistory> passwordHistoryList) {

        for (PasswordHistory passwordHistory : passwordHistoryList) {
            if (passwordEncoder.matches(newPassword, passwordHistory.getPassword())) {
                throw new InvalidPasswordException("새 비밀번호는 최근 사용한 비밀번호와 달라야 합니다.");
            }
        }
    }
}