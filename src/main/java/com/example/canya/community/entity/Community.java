package com.example.canya.community.entity;


import com.example.canya.community.dto.CommunityRequestDto;
import com.example.canya.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.example.canya.member.entity.Member;
import com.example.canya.timestamp.Timestamp;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Community extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long communityId;
    private String communityTitle;
    private String communityContent;
    private String communityImage;
    private Integer communityHitCount;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Community (CommunityRequestDto communityRequestDto, Member member, String image) {
        this.communityTitle = communityRequestDto.getCommunityTitle();
        this.communityContent = communityRequestDto.getCommunityContent();
        this.member = member;
        this.communityImage = image;
    }

    public void update(CommunityRequestDto communityRequestDto, String image) {
        this.communityTitle = communityRequestDto.getCommunityTitle();
        this.communityContent = communityRequestDto.getCommunityContent();
        this.communityImage = image;
    }
}
