package com.example.canya.Comment.Repository;

import com.example.canya.Comment.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByMember_MemberId(Long memberId);
}
