package com.example.canya.heart.repository;

import com.example.canya.board.entity.Board;
import com.example.canya.heart.entity.Heart;
import com.example.canya.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {
    boolean existsByBoardAndMember_MemberId(Board board, Long memberId);
    void deleteByBoardAndMember(Board board, Member member);
    Slice<Heart> findAllByMember_MemberIdOrderByCreatedAtDesc(Long memberId, Pageable pageable);
    List<Heart> findTop3ByMember_MemberIdOrderByCreatedAtDesc(Long memberId);
}
