package com.sparta.deventer.controller;

import com.sparta.deventer.dto.CommentResponseDto;
import com.sparta.deventer.dto.LikeRequestDto;
import com.sparta.deventer.dto.PostResponseDto;
import com.sparta.deventer.security.UserDetailsImpl;
import com.sparta.deventer.service.LikeService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/likes")
    public ResponseEntity<String> isLike(@RequestBody @Valid LikeRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String message = likeService.likeComparison(requestDto.getContentType(),
                requestDto.getContentId(), userDetails.getUser());
        return ResponseEntity.ok().body(message);
    }

    @GetMapping("/users/{userId}/likes/posts")
    public ResponseEntity<List<PostResponseDto>> getLikedPostsByUser(@PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page) {
        List<PostResponseDto> response = likeService.getLikedPostsByUser(userId, page);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/users/{userId}/likes/comments")
    public ResponseEntity<List<CommentResponseDto>> getLikedCommentByUser(@PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page) {
        List<CommentResponseDto> response = likeService.getLikedCommentByUser(userId, page);
        return ResponseEntity.ok().body(response);
    }
}
