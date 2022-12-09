package com.example.canya.member.dto;

import com.example.canya.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MypageResponseDto {
    private List<MemberResponseDto> recentlyMyBoardList;
    private List<MemberResponseDto> recentlyMyHeartBoardList;
    private List<MemberResponseDto> recentlyMyCommentList;
    private List<MemberResponseDto> recentlyMyCommunityList;
    private List<MemberResponseDto> recentlyMyCommunityCommentList;
    private List<MemberResponseDto> myPageList;
    private String memberProfileImage;
    private Integer memberBoardCount;
    private Integer memberHeartCount;
    private Integer memberCommentCount;
    private Integer memberCommunityCount;
    private Integer memberCommunityCommentCount;
    private Boolean isLast;

    public MypageResponseDto(List<MemberResponseDto> recentlyMyBoardList,
                             List<MemberResponseDto> recentlyMyHeartBoardList,
                             List<MemberResponseDto> recentlyMyCommentList,
                             List<MemberResponseDto> recentlyMyCommunityList,
                             List<MemberResponseDto> recentlyMyCommunityCommentList,
                             Member member) {
        this.recentlyMyBoardList = recentlyMyBoardList;
        this.recentlyMyHeartBoardList = recentlyMyHeartBoardList;
        this.recentlyMyCommentList = recentlyMyCommentList;
        this.recentlyMyCommunityList = recentlyMyCommunityList;
        this.recentlyMyCommunityCommentList = recentlyMyCommunityCommentList;
        this.memberProfileImage = member.getMemberProfileImage();
        this.memberBoardCount = member.getBoard().size();
        this.memberHeartCount = member.getHeart().size();
        this.memberCommentCount = member.getComment().size();
        this.memberCommunityCount = member.getCommunity().size();
        this.memberCommunityCommentCount = member.getCommunityComment().size();
    }

    public MypageResponseDto(List<MemberResponseDto> myPageList, Boolean isLast) {
        this.myPageList = myPageList;
        this.isLast = isLast;
    }
}
