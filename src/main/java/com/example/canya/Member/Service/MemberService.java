package com.example.canya.Member.Service;

import com.example.canya.JWT.Dto.TokenDto;
import com.example.canya.JWT.JwtAuthFilter;
import com.example.canya.JWT.TokenProvider;
import com.example.canya.Member.Dto.MemberRequestDto;
import com.example.canya.Member.Entity.Authority;
import com.example.canya.Member.Entity.Member;
import com.example.canya.Member.Repository.MemberRepository;
import com.example.canya.RefreshToken.RefreshToken;
import com.example.canya.RefreshToken.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;


    private final String DEFAULT_IMAGE = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRLxVwdzaxNLKosglrrPJRFG8ojryDuVby2yL8J4zwn&s";

    public ResponseEntity<?> nameCheck(String memberName){
        Optional<Member> memberOptional = memberRepository.findByMemberName(memberName);
        if(memberOptional.isEmpty()){
            return new ResponseEntity<>("사용 가능 한 아이디입니다.",HttpStatus.OK);
        }
        return new ResponseEntity<>("중복 된 아이디 입니다", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> nicknameCheck(String memberNickname){
        Optional<Member> memberOptional = memberRepository.findByMemberNickname(memberNickname);
        if(memberOptional.isEmpty()){
            return new ResponseEntity<>("사용 가능 한 닉네임입니다.",HttpStatus.OK);
        }
        return new ResponseEntity<>("중복 된 닉네임 입니다", HttpStatus.BAD_REQUEST);
    }




    public ResponseEntity<?> signUp(MemberRequestDto requestDto){

        Member member = Member.builder()
                .memberName(requestDto.getMemberName())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .memberNickname(requestDto.getMemberNickname())
                .memberProfileImage(DEFAULT_IMAGE)
                .authority(Authority.ROLE_USER)
                .build();

        memberRepository.save(member);

        return new ResponseEntity<>("회원가입에 성공하셨습니다.", HttpStatus.CREATED);
    };


    public ResponseEntity<?> login(MemberRequestDto requestDto, HttpServletResponse response){
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//        Member member = memberRepository.findByMemberName(requestDto.getMemberName())
//                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다."));
        Optional<Member> member = memberRepository.findByMemberName(requestDto.getMemberName());
        if(member.isEmpty()){
            return new ResponseEntity<>("아이도 혹은 패스워드를 확인해주세요",HttpStatus.BAD_REQUEST);
        }

        if (!passwordEncoder.matches(requestDto.getPassword(), member.get().getPassword())){
            return new ResponseEntity<>("아이디 혹은 패스워드를 확인해주세요.", HttpStatus.BAD_REQUEST);
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.add(JwtAuthFilter.AUTHORIZATION_HEADER , JwtAuthFilter.BEARER_PREFIX + tokenDto.getAccessToken());
        httpHeaders.add("Refresh-Token" , tokenDto.getRefreshToken());
        httpHeaders.add("memberNickname",requestDto.getMemberNickname());


        return new ResponseEntity<>("로그인에 성공하셨습니다.", httpHeaders, HttpStatus.OK);
    }

}
