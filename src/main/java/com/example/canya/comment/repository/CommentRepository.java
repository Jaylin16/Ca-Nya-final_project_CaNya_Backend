package com.example.canya.comment.repository;

import com.example.canya.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByMember_MemberId(Long memberId);
    List<Comment> findTop3ByMember_MemberIdOrderByCreatedAtDesc(Long memberId);
}
