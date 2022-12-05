package com.example.canya.communityComment.entity;

import com.example.canya.community.entity.Community;
import com.example.canya.member.entity.Member;
import com.example.canya.timestamp.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class CommunityComment extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long communityCommentId;

    private String communityCommentContent;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "community_id")
    private Community community;
}
