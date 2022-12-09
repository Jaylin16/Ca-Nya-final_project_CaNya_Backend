package com.example.canya.rating.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingRequestDto {
    private double coffeeRate;

    private double dessertRate;

    private double priceRate;

    private double moodRate;
    private double kindnessRate;

    private double parkingRate;

    private double totalRating;

    public RatingRequestDto(double coffeeRate, double dessertRate,
                            double priceRate,double moodRate, double kindnessRate
            , double parkingRate) {
        this.coffeeRate = coffeeRate;
        this.dessertRate = dessertRate;
        this.priceRate = priceRate;
        this.kindnessRate = kindnessRate;
        this.moodRate = moodRate;
        this.parkingRate = parkingRate;
        this.totalRating = (coffeeRate + dessertRate + priceRate + moodRate + kindnessRate + parkingRate)/6;
    }
}