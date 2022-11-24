package com.example.canya.Board.Repository;

import com.example.canya.Board.Entity.Board;
import com.example.canya.Member.Entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

import static com.example.canya.Board.Entity.QBoard.board;
import static com.querydsl.core.types.Ops.LIKE;
import static org.apache.coyote.http11.Constants.a;
import static org.hibernate.loader.Loader.SELECT;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {


    List<Board> findAllByBoardId(Long id);

    @Override
    @EntityGraph(attributePaths = {"ratingList"})
    Optional<Board> findById(Long boardId);
    List<Board> findBoardByMemberMemberNicknameContaining(String keyword);
    Slice<Board> findBoardByMemberMemberNicknameContaining(String keyword,Pageable pageable);
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
    @EntityGraph(attributePaths = {"imageList"}) // 23 -> 17
    List<Board> findBoardsByHighestRatingContainingOrderByTotalHeartCountDesc(String ratingName, Pageable pageable);
    List<Board> findBoardsByHighestRatingContainingOrderByTotalHeartCountDesc(String ratingName);

}

