package com.example.canya.Comment.Dto;

import com.example.canya.Comment.Entity.Comment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponseDto {

    private String commentContent;

    public CommentResponseDto(Comment comment) {
        this.commentContent = comment.getCommentContent();
    }
}
