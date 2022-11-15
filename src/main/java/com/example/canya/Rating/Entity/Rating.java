package com.example.canya.Rating.Entity;

import com.example.canya.Board.Dto.BoardRequestDto;
import com.example.canya.Board.Entity.Board;
import com.example.canya.Member.Entity.Member;
import com.example.canya.Rating.Dto.RatingRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Rating {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;

    @Column
    private double coffeeRate;

    @Column
    private double dessertRate;

    @Column
    private double priceRate;

    @Column
    private double moodRate;
    @Column
    private double kindnessRate;

    @Column
    private double parkingRate;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="board_id")
    private Board board;

    @JsonIgnore
    private Long memberId;

    public Rating (RatingRequestDto requestDto, Board board, Member member){
        this.coffeeRate = requestDto.getCoffeeRate();
        this.dessertRate = requestDto.getDessertRate();
        this.priceRate = requestDto.getPriceRate();
        this.moodRate = requestDto.getMoodRate();
        this.kindnessRate = requestDto.getKindnessRate();
        this.parkingRate= requestDto.getParkingRate();
        this.board = board;
        this.memberId = member.getMemberId();
    }
    @JsonIgnore
    public double getTotalRating(){
        return (this.coffeeRate + this.dessertRate + this.priceRate + this.moodRate + this.kindnessRate + this.parkingRate)/6;
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