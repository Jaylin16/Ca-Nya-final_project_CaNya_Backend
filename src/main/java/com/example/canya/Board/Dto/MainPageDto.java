package com.example.canya.Board.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MainPageDto {
    private List<BoardResponseDto> coffeePick;
    private List<BoardResponseDto> moodPick;
    private List<BoardResponseDto> dessertPick;
    private List<BoardResponseDto> newDto;
    private List<BoardResponseDto> allDto;
    private List<BoardResponseDto> bestDto;

    public MainPageDto(List<BoardResponseDto> coffeePick,
                       List<BoardResponseDto> moodPick,
                       List<BoardResponseDto> dessertPick,
                       List<BoardResponseDto>  newDto,
                       List<BoardResponseDto> allDto,
                       List<BoardResponseDto> bestDto) {
        this.coffeePick = coffeePick;
        this.moodPick = moodPick;
        this.dessertPick =dessertPick;
        this.newDto = newDto;
        this.allDto = allDto;
        this.bestDto = bestDto;
    }
}
