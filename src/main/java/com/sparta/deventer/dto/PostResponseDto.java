package com.sparta.deventer.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.deventer.entity.Post;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostResponseDto {

    private Long postId;
    private String nickname;
    private String categoryTopic;
    private String title;
    private String content;
    private Long likeCount;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    @Builder
    public PostResponseDto(Post post) {
        this.postId = post.getId();
        this.nickname = post.getUser().getNickname();
        this.categoryTopic = post.getCategory().getTopic();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.likeCount = post.getLikeCount();
        this.createAt = post.getCreatedAt();
        this.updateAt = post.getUpdatedAt();
    }

    @QueryProjection
    public PostResponseDto(Long postId, String nickname, String categoryTopic, String title,
            String content, Long likeCount, LocalDateTime createAt, LocalDateTime updateAt) {
        this.postId = postId;
        this.nickname = nickname;
        this.categoryTopic = categoryTopic;
        this.title = title;
        this.content = content;
        this.likeCount = likeCount;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
