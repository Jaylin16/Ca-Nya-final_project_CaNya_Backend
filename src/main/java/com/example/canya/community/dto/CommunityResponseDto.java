package com.example.canya.community.dto;

import com.example.canya.community.entity.Community;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class CommunityResponseDto {
    private Long communityId;
    private String communityTitle;
    private String communityContent;
    private String communityImage;
    private Long communityHitCount;
    private String createdAt;
    private String memberNickname;
    private String memberProfileImage;


    public CommunityResponseDto(Community community) {
        this.communityId = community.getCommunityId();
        this.communityTitle = community.getCommunityTitle();
        this.communityContent = community.getCommunityContent();
        this.communityImage = community.getCommunityImage();
        this.communityHitCount = community.getCommunityHitCount();
        this.createdAt = community.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.memberNickname = community.getMember().getMemberNickname();
        this.memberProfileImage = community.getMember().getMemberProfileImage();
    }
}
