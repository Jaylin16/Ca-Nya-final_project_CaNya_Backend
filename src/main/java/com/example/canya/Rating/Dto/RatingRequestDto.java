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

}