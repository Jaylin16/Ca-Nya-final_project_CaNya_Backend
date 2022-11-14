package com.example.canya.Heart.Repository;

import com.example.canya.Board.Entity.Board;
import com.example.canya.Heart.Entity.Heart;
import com.example.canya.Member.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {
    boolean existsByBoardAndMember(Board board, Member member);
    void deleteByBoardAndMember(Board board, Member member);
}
