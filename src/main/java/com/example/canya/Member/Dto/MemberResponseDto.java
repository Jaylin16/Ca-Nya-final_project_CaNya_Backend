package com.example.canya.Member.Dto;

import com.example.canya.Board.Entity.Board;
import com.example.canya.Comment.Entity.Comment;
import com.example.canya.Image.Entity.Image;
import com.example.canya.Member.Entity.Member;
import com.example.canya.Rating.Entity.Rating;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // null값 제외한 나머지 값만 반환하는 어노테이션.
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

    public MemberResponseDto(Member member){
        this.memberId =member.getMemberId();
        this.memberNickname = member.getMemberNickname();
    }

}
