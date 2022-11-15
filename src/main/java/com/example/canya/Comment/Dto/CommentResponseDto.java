package com.example.canya.Comment.Dto;

import com.example.canya.Comment.Entity.Comment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponseDto {

    private Long commentId;
    private String commentContent;
    private Long memberId;
    private String memberNickname;

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.commentContent = comment.getCommentContent();
        this.memberId = comment.getMember().getMemberId();
        this.memberNickname = comment.getMember().getMemberNickname();
    }
}
