package com.sparta.deventer.dto;

import com.sparta.deventer.entity.Post;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PostResponseDto {

    private final String nickname;
    private final String categoryTopic;
    private final String title;
    private final String content;
    private final int likeCount;
    private final LocalDateTime createAt;
    private final LocalDateTime updateAt;

    public PostResponseDto(Post post) {
        this.nickname = post.getUser().getNickname();
        this.categoryTopic = post.getCategory().getTopic();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.likeCount = post.getLikeCount();
        this.createAt = post.getCreatedAt();
        this.updateAt = post.getUpdateAt();
    }
}
