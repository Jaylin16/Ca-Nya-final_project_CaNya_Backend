package com.example.canya.Rating.Dto;

import com.example.canya.Rating.Entity.Rating;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RatingResponseDto {
    private double totalRating;
    private List<String> highestRatings;
    public RatingResponseDto(Rating ratingList , List<String>highestRatings) {

        this.highestRatings = highestRatings;
        this.totalRating = (ratingList.getDessertRate()+
                ratingList.getCoffeeRate()+
                ratingList.getKindnessRate()+
                ratingList.getParkingRate()+
                ratingList.getPriceRate()+
                ratingList.getMoodRate())/6;
    }
}
