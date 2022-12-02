package com.example.canya.comment.dto;

import com.example.canya.comment.entity.Comment;
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
