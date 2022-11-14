package com.example.canya.Rating.Entity;

import com.example.canya.Board.Dto.BoardRequestDto;
import com.example.canya.Board.Entity.Board;
import com.example.canya.Rating.Dto.RatingRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;

    @Column
    private Long coffeeRate;

    @Column
    private Long dessertRate;

    @Column
    private Long priceRate;

    @Column
    private Long kindnessRate;

    @Column
    private Long moodRate;

    @Column
    private Long parkingRate;

    @ManyToOne
    @JoinColumn(name="board_id")
    private Board board;

    public Rating (RatingRequestDto requestDto, Board board){
        this.coffeeRate = requestDto.getCoffeeRate();
        this.dessertRate = requestDto.getDessertRate();
        this.priceRate = requestDto.getPriceRate();
        this.kindnessRate = requestDto.getKindnessRate();
        this.moodRate = requestDto.getMoodRate();
        this.parkingRate= requestDto.getParkingRate();
        this.board = board;
    }

    public Rating(Board board){
        this.board = board;
    }

//    public void update(BoardRequestDto dto) {
//        this.coffeeRate = dto.getCoffeeRate() != null ? dto.getCoffeeRate() : this.coffeeRate;
//        this.dessertRate = dto.getDessertRate()!= null ? dto.getDessertRate() : this.dessertRate;
//        this.priceRate = dto.getPriceRate()!= null ? dto.getPriceRate() : this.priceRate;
//        this.kindnessRate = dto.getKindnessRate()!= null ? dto.getKindnessRate() : this.kindnessRate;
//        this.moodRate = dto.getMoodRate()!= null ? dto.getMoodRate() : this.moodRate;
//        this.parkingRate= dto.getParkingRate()!= null ? dto.getParkingRate() : this.parkingRate;
//    }
}