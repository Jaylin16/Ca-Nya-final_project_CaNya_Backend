package com.example.canya.Rating.Dto;

import com.example.canya.Rating.Entity.Rating;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class RatingResponseDto {

//    private Long coffeeRating;
//    private Long moodRating;
//    private Long parkingRating;
//    private Long dessertRating;
//    private Long kindnessRating;
//    private Long priceRating;

    private double totalRating;

    private Map.Entry<String, Double> highestRating;
    private Map.Entry<String, Double> secondHighestRating;



    public RatingResponseDto(Map.Entry<String, Double> maxEntry, Map.Entry<String, Double> secondMaxEntry, Rating ratingList) {
        this.highestRating = maxEntry;
        this.secondHighestRating = secondMaxEntry;
        this.totalRating = (ratingList.getDessertRate()+
                ratingList.getCoffeeRate()+
                ratingList.getKindnessRate()+
                ratingList.getParkingRate()+
                ratingList.getPriceRate()+
                ratingList.getMoodRate())/6;
    }

}
