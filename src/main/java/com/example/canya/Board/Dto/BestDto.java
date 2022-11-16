package com.example.canya.Board.Dto;

import com.example.canya.Board.Entity.Board;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BestDto {
    private String boardTitle;
    private String imageUrl;
    private Long boardId;

    public BestDto(Board board) {
        this.boardId = board.getBoardId();
        this.boardTitle = board.getBoardTitle();
        this.imageUrl = board.getImageList().get(0).getImageUrl();
    }
}
