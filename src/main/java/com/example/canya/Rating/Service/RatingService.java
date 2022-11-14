package com.example.canya.Rating.Service;

import com.example.canya.Board.Entity.Board;
import com.example.canya.Board.Repository.BoardRepository;
import com.example.canya.Rating.Dto.RatingRequestDto;
import com.example.canya.Rating.Entity.Rating;
import com.example.canya.Rating.Repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;

    private final BoardRepository boardRepository;
    @Transactional
    public ResponseEntity<?> rating(RatingRequestDto requestDto, Long boardId){
        System.out.println("boardId in service= " + boardId);
        Optional<Board> board = boardRepository.findById(boardId);
        if(board.isEmpty()){
            return new ResponseEntity<>("요청 하신 게시글이 존재하지않습니다.", HttpStatus.BAD_REQUEST);
        }


        Rating rating = new Rating(requestDto,board.get());
        ratingRepository.save(rating);

        return new ResponseEntity<>(requestDto,HttpStatus.CREATED);

    }

}