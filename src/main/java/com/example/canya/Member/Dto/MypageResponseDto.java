package com.example.canya.Member.Dto;

import lombok.Getter;
import java.util.List;

@Getter
public class MypageResponseDto {


    private final List<MemberResponseDto> recentlyMyBoardList;

    private final List<MemberResponseDto> recentlyMyHeartBoardList;

    private final List<MemberResponseDto> recentlyMyCommentList;


    public MypageResponseDto(List<MemberResponseDto> recentlyMyBoardList, List<MemberResponseDto> recentlyMyHeartBoardList, List<MemberResponseDto> recentlyMyCommentList) {
        this.recentlyMyBoardList = recentlyMyBoardList;
        this.recentlyMyHeartBoardList = recentlyMyHeartBoardList;
        this.recentlyMyCommentList = recentlyMyCommentList;
    }

}
