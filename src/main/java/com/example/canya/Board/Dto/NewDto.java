package com.example.canya.Board.Dto;

import com.example.canya.Board.Entity.Board;
import com.example.canya.Rating.Dto.RatingResponseDto;
import com.example.canya.Rating.Entity.Rating;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@Getter
@Setter
public class NewDto {
    private Long boardId;
    private Map.Entry<String, Double> highestRating;
    private Map.Entry<String, Double> secondHighestRating;
    private String imageUrl;

    public NewDto(RatingResponseDto dto, Board board) {
        this.boardId = board.getBoardId();
        this.highestRating = dto.getHighestRating();
        this.secondHighestRating = dto.getSecondHighestRating();
        this.imageUrl = board.getImageList().get(0).getImageUrl();
    }
}
