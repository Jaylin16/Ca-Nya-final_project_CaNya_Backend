package com.example.canya.member.dto;

import com.example.canya.board.entity.Board;
import com.example.canya.comment.entity.Comment;
import com.example.canya.community.entity.Community;
import com.example.canya.communityComment.entity.CommunityComment;
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

    //Member 관련 필드
    private Long memberId;
    private String memberNickname;

    //Board 관련 필드
    private Long boardId;
    private String boardTitle;
    private String boardContent;
    private String address;
    private String boardCreatedAt;
    private String totalRating;
    private List<Image> imageList = new ArrayList<>();
    private List<Rating> ratingList = new ArrayList<>();

    //Comment 관련 필드
    private Long commentId;
    private String commentContent;
    private Integer commentCount;
    private String commentCreatedAt;

    //Heart 관련 필드
    private Integer heartCount;

    //Community 관련 필드
    private Long communityId;
    private String communityTitle;
    private String communityContent;
    private String communityImage;
    private String communityCreatedAt;

    //CommunityComment 관련 필드
    private Long communityCommentId;
    private String communityCommentContent;
    private String communityCommentCreatedAt;


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

    public MemberResponseDto(Community community) {
        this.communityId = community.getCommunityId();
        this.communityTitle = community.getCommunityTitle();
        this.communityContent = community.getCommunityContent();
        this.communityImage = community.getCommunityImage();
        this.communityCreatedAt = community.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public MemberResponseDto(CommunityComment communityComment) {
        this.communityId = communityComment.getCommunity().getCommunityId();
        this.communityTitle = communityComment.getCommunity().getCommunityTitle();
        this.communityContent = communityComment.getCommunity().getCommunityContent();
        this.communityCommentId = communityComment.getCommunityCommentId();
        this.communityCommentContent = communityComment.getCommunityCommentContent();
        this.communityCommentCreatedAt = communityComment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
