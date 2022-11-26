package com.example.canya.Rating.Entity;

import com.example.canya.Board.Entity.Board;
import com.example.canya.Member.Entity.Member;
import com.example.canya.Rating.Dto.RatingRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public List<String> getTwoHighestRatings(Rating ratingList){
        HashMap<String, Double> ratingMap = new HashMap<String, Double>();
        ratingMap.put("커피 맛집", ratingList.getCoffeeRate());
        ratingMap.put("디저트 맛집", ratingList.getDessertRate());
        ratingMap.put("친절 맛집", ratingList.getKindnessRate());
        ratingMap.put("분위기 맛집", ratingList.getMoodRate());
        ratingMap.put("주차 맛집", ratingList.getParkingRate());
        ratingMap.put("가성비 맛집", ratingList.getPriceRate());
        List<Map.Entry<String, Double>> entries = ratingMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());
        List<String> twoRatings = new ArrayList<>();
        twoRatings.add(String.valueOf(entries.get(5).getKey()));
        twoRatings.add(String.valueOf(entries.get(4).getKey()));
    return twoRatings;
    }

    public void update(RatingRequestDto ratingDto) {
        this.coffeeRate = ratingDto.getCoffeeRate();
        this.dessertRate = ratingDto.getDessertRate();
        this.priceRate = ratingDto.getPriceRate();
        this.moodRate = ratingDto.getMoodRate();
        this.kindnessRate = ratingDto.getKindnessRate();
        this.parkingRate= ratingDto.getParkingRate();
    }
}