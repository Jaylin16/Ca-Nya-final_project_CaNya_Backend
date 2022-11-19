package com.example.canya.Board.Repository;

import com.example.canya.Board.Entity.Board;
import com.example.canya.Member.Entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {

    List<Board> findBoardsByBoardContentContaining(String keyword);
    Slice<Board> findBoardsByBoardContentContaining(String keyword, Pageable pageable);
    List<Board> findBoardsByBoardTitleContaining(String keyword);
    Slice<Board> findBoardsByBoardTitleContaining(String keyword, Pageable pageable);
    List<Board> findBoardByMember(Member member);
    Slice<Board> findBoardByMember(Member member, Pageable pageable);
    List<Board> findTop6ByOrderByCreatedAtDesc();
    List<Board> findTop4ByOrderByTotalHeartCountDesc();
    List<Board> findTop8ByOrderByCreatedAtDesc();
    List<Board> findTop3ByMember_MemberIdOrderByCreatedAtDesc(Long memberId);
    List<Board> findTop3ByHighestRatingContainingOrderByTotalHeartCountDesc(String ratingName);
    Slice<Board> findBoardsByHighestRatingContainingOrderByTotalHeartCountDesc(String ratingName, Pageable pageable);
    List<Board> findBoardsByHighestRatingContainingOrderByTotalHeartCountDesc(String ratingName);

}
