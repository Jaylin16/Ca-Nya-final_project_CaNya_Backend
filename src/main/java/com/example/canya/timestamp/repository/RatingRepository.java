package com.example.canya.timestamp.repository;

import com.example.canya.board.entity.Board;
import com.example.canya.rating.entity.Rating;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    @EntityGraph(attributePaths = {"board"})
    Rating findRatingByBoardAndMemberId(Board board, Long memberId);
}