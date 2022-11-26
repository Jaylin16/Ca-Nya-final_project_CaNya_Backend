package com.example.canya.Board.Dto;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

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
}
