package com.sparta.deventer.dto;

import com.sparta.deventer.entity.Post;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
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
}
