package com.example.canya.member.dto;

import com.example.canya.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MypageResponseDto {
    private final List<MemberResponseDto> recentlyMyBoardList;
    private final List<MemberResponseDto> recentlyMyHeartBoardList;
    private final List<MemberResponseDto> recentlyMyCommentList;
    private final String memberProfileImage;
    private final Integer memberBoardCount;
    private final Integer memberHeartCount;
    private final Integer memberCommentCount;

    public MypageResponseDto(List<MemberResponseDto> recentlyMyBoardList, List<MemberResponseDto> recentlyMyHeartBoardList, List<MemberResponseDto> recentlyMyCommentList, Member member) {
        this.recentlyMyBoardList = recentlyMyBoardList;
        this.recentlyMyHeartBoardList = recentlyMyHeartBoardList;
        this.recentlyMyCommentList = recentlyMyCommentList;
        this.memberProfileImage = member.getMemberProfileImage();
        this.memberBoardCount = member.getBoard().size();
        this.memberHeartCount = member.getHeart().size();
        this.memberCommentCount = member.getComment().size();
    }
}
