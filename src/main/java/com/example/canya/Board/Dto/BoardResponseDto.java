package com.example.canya.Board.Dto;

import com.example.canya.Board.Entity.Board;
import com.example.canya.Image.Entity.Image;

import com.example.canya.Rating.Entity.Rating;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoardResponseDto {
    private Long boardId;
    private String boardTitle;
    private String boardContent;
    private String address;
    private int heartCount;
    private int commentCount;
    private String imageUrl;
    private String formData;
    private LocalDateTime createdAt;
    private Rating rating;
    private String totalRating;
    private String memberNickname;
    private String memberProfileImg;
    private boolean isLiked;

    private List<Image> imageList;
    private Map.Entry<String, Double> highestRating;
    private Map.Entry<String, Double> secondHighestRating;

    @Builder
    public BoardResponseDto(Board board, List<Image> imageList, Rating rating, boolean isLiked){
        this.boardContent = board.getBoardContent();
        this.boardId = board.getBoardId();
        this.boardTitle = board.getBoardTitle();
        this.createdAt = board.getCreatedAt();
        this.rating = rating;
        this.heartCount = board.getHeartCount();
        this.isLiked = isLiked;
        this.memberNickname = board.getMember().getMemberNickname();
        this.memberProfileImg = board.getMember().getMemberProfileImage();
        this.address = board.getAddress();
        this.imageList = imageList;
        this.totalRating = String.format("%.1f",rating.getTotalRating());
    }

}
