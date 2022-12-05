package com.example.canya.communityComment.repository;

import com.example.canya.communityComment.entity.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {
}
