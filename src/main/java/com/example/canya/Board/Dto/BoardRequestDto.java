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
    private double totalRating;

    public double getTotalRating (Long[] ratings){
        this.totalRating = 0;
        for (Long rating : ratings) {
            totalRating+=rating;
        }
        return totalRating;
    }

}
