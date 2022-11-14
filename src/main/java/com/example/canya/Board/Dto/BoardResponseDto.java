package com.example.canya.Board.Dto;

import com.example.canya.Board.Entity.Board;
import com.example.canya.Image.Entity.Image;
import com.example.canya.Rating.Dto.RatingResponseDto;
import com.example.canya.Rating.Entity.Rating;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class BoardResponseDto {
    private String boardTitle;
    private String boardContent;
    private int heartCount;
    private int commentCount;
    private String imageUrl;
    private LocalDateTime createdAt;
    private Map.Entry<String, Long> highestRating;
    private Map.Entry<String, Long> secondHighestRating;

    public BoardResponseDto(Board board) {
        this.createdAt = board.getCreatedAt();
        this.boardTitle = board.getBoardTitle();
        this.boardContent = board.getBoardContent();

    }
    public BoardResponseDto(Board board, String image,RatingResponseDto dto) {
        this.createdAt = board.getCreatedAt();
        this.boardTitle = board.getBoardTitle();
        this.boardContent = board.getBoardContent();
        this.imageUrl = image;
        this.highestRating = dto.getHighestRating();
        this.secondHighestRating = dto.getSecondHighestRating();
    }
    public BoardResponseDto(Board board, String image) {
        this.createdAt = board.getCreatedAt();
        this.boardTitle = board.getBoardTitle();
        this.boardContent = board.getBoardContent();
        this.imageUrl = image;
    }
}
