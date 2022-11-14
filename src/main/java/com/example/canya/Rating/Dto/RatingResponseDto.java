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

    private Map.Entry<String, Long> highestRating;
    private Map.Entry<String, Long> secondHighestRating;


    public RatingResponseDto(Map.Entry<String, Long> maxEntry, Map.Entry<String, Long> secondMaxEntry) {
        this.highestRating = maxEntry;
        this.secondHighestRating = secondMaxEntry;
    }
}
