package com.example.canya.member.dto;

import com.example.canya.board.entity.Board;
import com.example.canya.comment.entity.Comment;
import com.example.canya.image.entity.Image;
import com.example.canya.rating.entity.Rating;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberResponseDto {

    private Long boardId;
    private Long memberId;
    private String boardTitle;
    private String memberNickname;
    private String boardContent;
    private String address;
    private String boardCreatedAt;
    private Long commentId;
    private String commentContent;
    private Integer commentCount;
    private String commentCreatedAt;
    private String totalRating;
    private Integer heartCount;
    private List<Rating> ratingList = new ArrayList<>();
    private List<Image> imageList = new ArrayList<>();


    public MemberResponseDto(Board board) {
        this.boardId = board.getBoardId();
        this.boardTitle = board.getBoardTitle();
        this.boardContent = board.getBoardContent();
        this.boardCreatedAt = board.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.address = board.getAddress();
        this.totalRating = String.format("%.1f",board.getRatingList().get(ratingList.size()).getTotalRating());
        this.imageList = board.getImageList();
        this.commentCount = board.getCommentList().size();
        this.heartCount = board.getHeartCount();
    }

    public MemberResponseDto(Comment comment) {
        this.boardId = comment.getBoard().getBoardId();
        this.boardTitle = comment.getBoard().getBoardTitle();
        this.boardContent = comment.getBoard().getBoardContent();
        this.commentId = comment.getCommentId();
        this.commentContent = comment.getCommentContent();
        this.commentCreatedAt = comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}