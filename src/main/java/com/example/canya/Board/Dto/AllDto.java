package com.example.canya.Board.Dto;

import com.example.canya.Board.Entity.Board;
import com.example.canya.Rating.Dto.RatingResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class AllDto {

    private String address;
    private String imageUrl;
    private String totalRating;
    private Map.Entry<String, Double> highestRating;
    private Map.Entry<String, Double> secondHighestRating;

    public AllDto(Board board, RatingResponseDto ratingDto){
        this.address = board.getAddress();
        this.imageUrl = board.getImageList().get(0).getImageUrl();
        this.highestRating = ratingDto.getHighestRating();
        this.secondHighestRating = ratingDto.getSecondHighestRating();
        this.totalRating = String.format("%.1f",ratingDto.getTotalRating());
    }
}
