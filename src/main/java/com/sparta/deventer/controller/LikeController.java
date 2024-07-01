package com.sparta.deventer.controller;

import com.sparta.deventer.dto.LikeRequestDto;
import com.sparta.deventer.security.UserDetailsImpl;
import com.sparta.deventer.service.LikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
}
