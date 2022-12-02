package com.example.canya.community.entity;

import com.example.canya.member.entity.Member;
import com.example.canya.timestamp.Timestamp;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
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
}
