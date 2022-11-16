package com.example.canya.Board.Dto;

import com.example.canya.Board.Entity.Board;
import com.example.canya.Rating.Dto.RatingResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@Getter
@Setter
public class CoffeePick {
    private Long boardId;
    private String boardTitle;
    private String boardContent;
    private int heartCount;
    private int commentCount;
    private double totalRating;
    private String imageUrl;
    private Map.Entry<String, Double> highestRating;
    private Map.Entry<String, Double> secondHighestRating;

    public CoffeePick(Board board, RatingResponseDto dto) {
        this.boardId = board.getBoardId();
        this.boardTitle = board.getBoardTitle();
        this.boardContent = board.getBoardContent();
        this.commentCount = board.getCommentList().size();
        this.heartCount = board.getHeartList().size();
        this.totalRating = dto.getTotalRating();
        this.imageUrl = board.getImageList().get(0).getImageUrl();
        this.highestRating = dto.getHighestRating();
        this.secondHighestRating = dto.getSecondHighestRating();
    }
}
