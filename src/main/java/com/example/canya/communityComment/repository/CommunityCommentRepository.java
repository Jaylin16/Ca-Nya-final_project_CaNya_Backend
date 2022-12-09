package com.example.canya.communityComment.repository;

import com.example.canya.communityComment.entity.CommunityComment;
import com.example.canya.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {
    List<CommunityComment> findByCommunity_CommunityId(Long communityId);
    Slice<CommunityComment> findCommunityCommentByMember(Member member, Pageable pageable);
    List<CommunityComment> findTop3ByMember_MemberIdOrderByCreatedAtDesc(Long memberId);
}
