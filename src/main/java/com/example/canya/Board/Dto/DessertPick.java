package com.example.canya.Board.Dto;

import com.example.canya.Board.Entity.Board;
import com.example.canya.Member.Entity.Member;
import com.example.canya.Rating.Dto.RatingResponseDto;
import com.example.canya.Timestamp.Time;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class DessertPick {
    private Long boardId;
    private String boardTitle;
    private String boardContent;
    private String address;
    private String memberProfileImage;
    private String memberNickname;
    private String date;
    private int heartCount;
    private int commentCount;
    private String totalRating;
    private String imageUrl;
    private Map.Entry<String, Double> highestRating;
    private Map.Entry<String, Double> secondHighestRating;

    public DessertPick(Board board,  RatingResponseDto dto) {
        this.memberNickname = board.getMember().getMemberNickname();
        this.memberProfileImage = board.getMember().getMemberProfileImage();
        this.address = board.getAddress();
        this.boardId = board.getBoardId();
        this.date = Time.calculateTime(board);
        this.boardTitle = board.getBoardTitle();
        this.boardContent = board.getBoardContent();
        this.commentCount = board.getCommentList().size();
        this.heartCount = board.getHeartList().size();
        this.totalRating = String.format("%.1f",dto.getTotalRating());
        this.imageUrl = board.getImageList().get(0).getImageUrl();
        this.highestRating = dto.getHighestRating();
        this.secondHighestRating = dto.getSecondHighestRating();
    }
}
