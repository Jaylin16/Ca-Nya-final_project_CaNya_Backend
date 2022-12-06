package com.example.canya.communityComment.dto;

import com.example.canya.communityComment.entity.CommunityComment;
import com.example.canya.timestamp.Time;
import lombok.Getter;

@Getter
public class CommunityCommentResponseDto {

    private Long communityCommentId;
    private String communityCommentContent;
    private Long memberId;
    private String memberNickname;
    private String memberProfileImage;
    private String date;


    public CommunityCommentResponseDto(CommunityComment communityCommentList) {
        this.communityCommentId = communityCommentList.getCommunityCommentId();
        this.communityCommentContent = communityCommentList.getCommunityCommentContent();
        this.memberId = communityCommentList.getMember().getMemberId();
        this.memberNickname = communityCommentList.getMember().getMemberNickname();
        this.memberProfileImage = communityCommentList.getMember().getMemberProfileImage();
        this.date = Time.calculateTime(communityCommentList);
    }
}
