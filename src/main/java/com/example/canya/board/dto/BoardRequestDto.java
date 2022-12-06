package com.example.canya.board.dto;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardRequestDto {
    @NotNull
    private String boardTitle;
    @NotNull
    private String boardContent;
    @NotNull
    private String address;
    @NotNull
    private Long[] ratings;
    @NotNull
    private String addressId;
}
