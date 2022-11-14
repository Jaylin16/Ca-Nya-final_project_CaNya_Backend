package com.example.canya.Rating.Repository;

import com.example.canya.Board.Entity.Board;
import com.example.canya.Rating.Entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findRatingByBoardAndMemberId(Board board,Long memberId);
}