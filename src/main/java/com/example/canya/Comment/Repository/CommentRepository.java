package com.example.canya.comment.repository;

import com.example.canya.comment.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Slice<Comment> findAllByMember_MemberId(Long memberId, Pageable pageable);
    List<Comment> findAllByBoard_BoardId(Long boardId);
    List<Comment> findTop3ByMember_MemberIdOrderByCreatedAtDesc(Long memberId);
}