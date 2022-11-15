package com.example.canya.Rating.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingRequestDto {

    private Long coffeeRate;

    private Long dessertRate;

    private Long priceRate;

    private Long kindnessRate;

    private Long moodRate;

    private Long parkingRate;

    public RatingRequestDto(Long coffeeRate, Long dessertRate,
                            Long priceRate,Long moodRate, Long kindnessRate
            , Long parkingRate) {
        this.coffeeRate = coffeeRate;
        this.dessertRate = dessertRate;
        this.priceRate = priceRate;
        this.kindnessRate = kindnessRate;
        this.moodRate = moodRate;
        this.parkingRate = parkingRate;
    }

}