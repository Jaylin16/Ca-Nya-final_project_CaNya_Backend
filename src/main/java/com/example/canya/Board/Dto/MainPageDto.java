package com.example.canya.Board.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MainPageDto {
    private List<CanyaPickDto> canyaPickDto;
    private List<NewDto> newDto;
    private List<AllDto> allDto;
    private List<BestDto> bestDto;

    public MainPageDto(List<CanyaPickDto> canyaPickDto, List<NewDto>  newDto, List<AllDto> allDto, List<BestDto> bestDto) {
        this.canyaPickDto = canyaPickDto;
        this.newDto = newDto;
        this.allDto = allDto;
        this.bestDto = bestDto;
    }
}
