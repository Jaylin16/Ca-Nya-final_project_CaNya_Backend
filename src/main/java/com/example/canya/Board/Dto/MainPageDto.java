package com.example.canya.Board.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MainPageDto {
    private List<CoffeePick> coffeePick;
    private List<MoodPick> moodPick;
    private List<DessertPick> dessertPick;
    private List<NewDto> newDto;
    private List<AllDto> allDto;
    private List<BestDto> bestDto;

    public MainPageDto(List<CoffeePick> coffeePick, List<MoodPick> moodPick, List<DessertPick> dessertPick , List<NewDto>  newDto, List<AllDto> allDto, List<BestDto> bestDto) {
        this.coffeePick = coffeePick;
        this.moodPick = moodPick;
        this.dessertPick =dessertPick;
        this.newDto = newDto;
        this.allDto = allDto;
        this.bestDto = bestDto;
    }
}
