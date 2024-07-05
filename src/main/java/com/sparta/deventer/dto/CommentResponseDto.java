package com.sparta.deventer.dto;


import com.querydsl.core.annotations.QueryProjection;
import com.sparta.deventer.entity.Comment;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponseDto {

    private String content;
    private Long id;
    private Long userId;
    private String nickname;
    private Long likeCount;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;


    public CommentResponseDto(Comment comment) {
        this.content = comment.getContent();
        this.id = comment.getId();
        this.userId = comment.getUser().getId();
        this.nickname = comment.getUser().getNickname();
        this.likeCount = comment.getLikeCount();
        this.createAt = comment.getCreatedAt();
        this.updateAt = comment.getUpdatedAt();
    }

    @QueryProjection
    public CommentResponseDto(String content, Long id, Long userId, String nickname, Long likeCount,
            LocalDateTime createAt, LocalDateTime updateAt) {
        this.content = content;
        this.id = id;
        this.userId = userId;
        this.nickname = nickname;
        this.likeCount = likeCount;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
