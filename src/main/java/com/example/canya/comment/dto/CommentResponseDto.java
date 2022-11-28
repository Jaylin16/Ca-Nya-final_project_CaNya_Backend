package com.example.canya.comment.dto;

import com.example.canya.comment.entity.Comment;
import lombok.Getter;
import lombok.Setter;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class CommentResponseDto {

    private Long commentId;
    private String commentContent;
    private Long memberId;
    private String memberNickname;
    private String createdAt;

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.commentContent = comment.getCommentContent();
        this.memberId = comment.getMember().getMemberId();
        this.memberNickname = comment.getMember().getMemberNickname();
        this.createdAt = comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
