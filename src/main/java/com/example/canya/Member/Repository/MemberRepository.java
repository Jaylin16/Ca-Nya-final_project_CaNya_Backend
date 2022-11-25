package com.example.canya.Member.Repository;

import com.example.canya.Member.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberName(String MemberName);

    Optional<Member> findByMemberNickname(String MemberNickname);


}
