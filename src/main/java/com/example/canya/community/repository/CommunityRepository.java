package com.example.canya.community.repository;

import com.example.canya.community.entity.Community;
import com.example.canya.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {
    Slice<Community> findCommunityByMember(Member member, Pageable pageable);
    List<Community> findTop3ByMember_MemberIdOrderByCreatedAtDesc(Long memberId);
}
