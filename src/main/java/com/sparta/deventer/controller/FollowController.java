package com.sparta.deventer.controller;

import com.sparta.deventer.dto.FollowRequestDto;
import com.sparta.deventer.dto.FollowResponseDto;
import com.sparta.deventer.security.UserDetailsImpl;
import com.sparta.deventer.service.FollowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/follows")
    public ResponseEntity<FollowResponseDto> followUser(
            @Valid @RequestBody FollowRequestDto requestDto, @AuthenticationPrincipal
    UserDetailsImpl userDetails) {
        return ResponseEntity.ok()
                .body(followService.followUser(requestDto, userDetails.getUser()));
    }

    @DeleteMapping("/follows")
    public ResponseEntity<String> unFollowUser(@Valid @RequestBody FollowRequestDto requestDto,
            @AuthenticationPrincipal
            UserDetailsImpl userDetails) {
        return ResponseEntity.ok()
                .body(followService.unFollowUser(requestDto, userDetails.getUser()));
    }
}
