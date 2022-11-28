package com.example.canya.board.repository;

import com.example.canya.board.entity.Board;
import com.example.canya.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {
    List<Board> findBoardByMemberMemberNicknameContaining(String keyword);
    Slice<Board> findBoardByMemberMemberNicknameContaining(String keyword,Pageable pageable);
    List<Board> findBoardsByBoardContentContaining(String keyword);
    Slice<Board> findBoardsByBoardContentContaining(String keyword, Pageable pageable);
    List<Board> findBoardsByBoardTitleContaining(String keyword);
    Slice<Board> findBoardsByBoardTitleContaining(String keyword, Pageable pageable);
    List<Board> findBoardByMember(Member member);
    List<Board> findTop6ByIsReadyTrueOrderByCreatedAtDesc();
    List<Board> findTop4ByIsReadyTrueOrderByTotalHeartCountDesc();
    List<Board> findTop8ByIsReadyTrueOrderByCreatedAtDesc();
    List<Board> findTop3ByMember_MemberIdOrderByCreatedAtDesc(Long memberId);
    List<Board> findTop3ByHighestRatingContainingOrderByTotalHeartCountDesc(String ratingName);
    @EntityGraph(attributePaths = {"imageList"}) // 21
    List<Board> findBoardsByHighestRatingContainingOrderByTotalHeartCountDesc(String ratingName, Pageable pageable);
    List<Board> findBoardsByHighestRatingContainingOrderByTotalHeartCountDesc(String ratingName);
}

