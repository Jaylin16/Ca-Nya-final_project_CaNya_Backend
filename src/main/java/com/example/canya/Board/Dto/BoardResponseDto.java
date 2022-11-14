package com.example.canya.Board.Dto;

import com.example.canya.Board.Entity.Board;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardResponseDto {
    private String boardTitle;
    private String boardContent;
    private int heartCount;
    private int commentCount;
    private String imageUrl;

    public BoardResponseDto(Board board) {
        this.boardTitle = board.getBoardTitle();
        this.boardContent = board.getBoardContent();
    }
}
