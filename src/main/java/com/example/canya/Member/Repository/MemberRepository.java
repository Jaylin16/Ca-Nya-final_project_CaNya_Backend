package com.example.canya.Member.Repository;

import com.example.canya.Board.Entity.Board;
import com.example.canya.Member.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberName(String MemberName);

    List<Member> findMembersByMemberNicknameContaining(String keyword);
    Optional<Member> findByMemberNickname(String MemberNickname);


}
