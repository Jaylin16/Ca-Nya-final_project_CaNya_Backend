package com.example.canya.Board.Dto;

import com.example.canya.Board.Entity.Board;
import com.example.canya.Rating.Dto.RatingResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class CanyaPickDto {
    private List<CoffeePick> coffeePick;
    private List<DessertPick> dessertPick;
    private List<MoodPick> moodPick;

    public CanyaPickDto(List<CoffeePick> coffeePick, List<DessertPick> dessertPick, List<MoodPick> moodPick) {
        this.coffeePick = coffeePick;
        this.dessertPick = dessertPick;
        this.moodPick = moodPick;
    }
}
