package com.example.canya.Heart.Repository;

import com.example.canya.Board.Entity.Board;
import com.example.canya.Heart.Entity.Heart;
import com.example.canya.Member.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Optional;



@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {
    boolean existsByBoardAndMember_MemberId(Board board, Long memberId);
    void deleteByBoardAndMember(Board board, Member member);


    List<Heart>findAllByBoard(Board board);

    List<Heart> findAllByMember_MemberId(Long memberId);

}
