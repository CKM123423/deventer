package com.sparta.deventer.controller;

import com.sparta.deventer.dto.ScrapResponseDto;
import com.sparta.deventer.security.UserDetailsImpl;
import com.sparta.deventer.service.ScrapService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/scraps")
public class ScrapController {

    private final ScrapService scrapService;

    @PostMapping("/posts/{postsId}")
    public ResponseEntity<String> scrapEitherOne(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long postsId) {
        boolean isScarp = scrapService.scrapEitherOne(userDetails.getUser(), postsId);
        return ResponseEntity.ok().body(isScarp ? "스크랩 했습니다." : "스크랩 취소 했습니다");
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<ScrapResponseDto>> getUserScraps(@PathVariable Long userId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<ScrapResponseDto> scrapList = scrapService.getUserScraps(userId,
            userDetails.getUser());
        return ResponseEntity.ok(scrapList);
    }
}