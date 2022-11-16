package com.example.canya.Member.Dto;

import com.example.canya.Board.Entity.Board;
import com.example.canya.Comment.Entity.Comment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberResponseDto {

    private String boardTitle;
    private String boardContent;
    private String commentContent;


    public MemberResponseDto(Board board) {
        this.boardTitle = board.getBoardTitle();
        this.boardContent = board.getBoardContent();
    }

    public MemberResponseDto(Comment comment) {
        this.boardTitle = comment.getBoard().getBoardTitle();
        this.boardContent = comment.getBoard().getBoardContent();
        this.commentContent = comment.getCommentContent();
    }
}
