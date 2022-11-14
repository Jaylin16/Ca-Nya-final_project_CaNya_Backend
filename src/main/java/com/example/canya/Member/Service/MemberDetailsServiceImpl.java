package com.example.canya.Member.Service;

import com.example.canya.Member.Entity.Member;
import com.example.canya.Member.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;


    public UserDetails loadUserByUsername(String memberName) throws UsernameNotFoundException {
        Member member = memberRepository.findByMemberName(memberName)
                .orElseThrow(() -> new UsernameNotFoundException("Can't find " + memberName));

        return new MemberDetailsImpl( member );
    }
}