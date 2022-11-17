package com.example.canya.Board.Repository;

import com.example.canya.Board.Entity.Board;
import com.example.canya.Member.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {

    List<Board> findBoardByMember(Member member);
    List<Board> findAllByOrderByCreatedAtDesc();

    
//    List<Board> findAllByOrderByTotalRating();
    List<Board> findBoardsByHighestRatingContainingOrderByTotalHeartCountDesc(String ratingName);
    List<Board> findBoardsByOrderByTotalHeartCountDesc();

}
