package com.example.canya.Rating.Controller;

import com.example.canya.Rating.Dto.RatingRequestDto;
import com.example.canya.Rating.Service.RatingService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "Rating (별점)")
public class RatingController {

    private final RatingService ratingService;

    @Operation(summary = "게시물별 별점 추가", description = "각 게시물별 별점 생성 기능")
    @PostMapping("/auth/rating/{boardId}")
    public ResponseEntity<?> rating(@RequestBody RatingRequestDto dto, @PathVariable Long boardId){

        return ratingService.rating(dto, boardId);
    }

}