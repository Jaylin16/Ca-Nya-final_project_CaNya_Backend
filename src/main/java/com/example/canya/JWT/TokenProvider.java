package com.example.canya.JWT;

import com.example.canya.JWT.Dto.TokenDto;
import com.example.canya.Member.Entity.Member;
import com.example.canya.Member.Repository.MemberRepository;
import com.example.canya.Member.Service.MemberDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.canya.JWT.JwtAuthFilter.BEARER_PREFIX;

@Slf4j
@Component
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000L*60*60*24*365; // 365일.
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L*60*60*24*1000; // 1000일

    private final MemberRepository memberRepository;

    private final Key key;

    @Autowired
    public TokenProvider(@Value("${jwt.secret}") String secretKey, MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto generateTokenDto(Authentication authentication) {
        String authorities = authentication
                .getAuthorities()

                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long nowTime = new Date().getTime();
        Member member = memberRepository.findByMemberName(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("Can't find " + authentication.getName() ));;
        // 액세스 토큰 생성
        Date accessTokenExpires = new Date(nowTime + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                // paload 부분에 필드, 값 넣기
                .setSubject(authentication.getName())
                .setAudience(member.getMemberNickname())
                .claim("memberStatus", member.getStatus())
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(accessTokenExpires)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // 리프레쉬 토큰
        Date refreshTokenExpires = new Date(nowTime + REFRESH_TOKEN_EXPIRE_TIME);
        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .setExpiration(refreshTokenExpires)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpires.getTime())
                .refreshToken(refreshToken)
                .build();

    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다");
        }

        String memberName = claims.getSubject();
        Member member = memberRepository.findByMemberName(memberName)
                .orElseThrow(() -> new UsernameNotFoundException("Can't find " + memberName ));
        MemberDetailsImpl memberDetailsImpl = new MemberDetailsImpl(member);
        return new UsernamePasswordAuthenticationToken(memberDetailsImpl, null, memberDetailsImpl.getAuthorities());
    }

    public boolean validateToken(String jwt) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt);
            return true;

        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("JWT 올바르게 구성되지 않았습니다");
        } catch (ExpiredJwtException e) {
            log.info("JWT 유효시간이 초과되었습니다");
            tokenErrorHandler(e);
        } catch (UnsupportedJwtException e) {
            log.info("JWT 형식이 일치 하지 않습니다");
        } catch (PrematureJwtException e) {
            log.info("이 토큰은 아직 유효한 토큰이 아닙니다. 활성화 시기를 확인해 주십시오");
        } catch (ClaimJwtException e) {
            log.info("JWT PAYLOAD 분석에 실패했습니다");
        }
        return false;
    }

    // 이 부분, refresh token 이랑 연결 시켜보기.
    private ResponseEntity<?> tokenErrorHandler(ExpiredJwtException e) {
        return new ResponseEntity<>("토큰이 만료되었습니다. 다시 로그인해 주세요", HttpStatus.BAD_REQUEST);
    }

    // Security 에서 인증 유저 정보 가져오기
    public String getMemberNicknameInToken(String accessTokenTemp) {

        if(!StringUtils.hasText(accessTokenTemp) && accessTokenTemp.startsWith(BEARER_PREFIX)) {
            throw new RuntimeException("토큰에 아무것두 없어유");
        }
        String accessToken =  accessTokenTemp.substring(7);

        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다");
        }

        String memberName = claims.getSubject();
        Member member = memberRepository.findByMemberName(memberName)
                .orElseThrow(() -> new UsernameNotFoundException(memberName + "라는 유저가 없습니다"));

        return member.getMemberNickname();
    }

    public String getMemberFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Member> memberTemp = memberRepository.findByMemberName(authentication.getName());
        if (memberTemp.isEmpty()) {
            throw new RuntimeException();
        }
        Member member = memberTemp.get();
        return member.getMemberName();
    }
}