package com.example.canya.Board.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class BoardRequestDto {

    private String boardTitle;
    private String boardContent;
    private String address;
    private Long[] ratings;

//    private ArrayList<MultipartFile> image;



//    private Long coffeeRate;
//    private Long dessertRate;
//    private Long priceRate;
//    private Long kindnessRate;
//    private Long moodRate;
//    private Long parkingRate;

}
