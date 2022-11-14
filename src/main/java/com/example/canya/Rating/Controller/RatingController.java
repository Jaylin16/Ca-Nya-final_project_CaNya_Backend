package com.example.canya.Rating.Controller;


import com.example.canya.Board.Entity.Board;
import com.example.canya.Rating.Dto.RatingRequestDto;
import com.example.canya.Rating.Service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping("/auth/rating/{boardId}")
    public ResponseEntity<?> rating(@RequestBody RatingRequestDto dto, @PathVariable Long boardId){
        System.out.println("boardId = " + boardId);
        return ratingService.rating(dto, boardId);
    }

}