package com.example.canya.communityComment.repository;

import com.example.canya.communityComment.entity.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {
    List<CommunityComment> findByCommunity_CommunityId(Long communityId);
}
