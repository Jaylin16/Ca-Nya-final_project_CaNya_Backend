package com.example.canya.Member.Dto;

import com.example.canya.Member.Entity.Authority;
import com.example.canya.Member.Entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MemberRequestDto {

    private Long memberId;
    private String memberName;
    private String password;

    private String memberProfileImage;
    private String memberNickname;
    @JsonIgnore
    private Authority authority;

    public MemberRequestDto(Member member){
        this.memberId = member.getMemberId();
        this.memberName = member.getMemberName();
        this.password = member.getPassword();
        this.memberNickname = member.getMemberNickname();
        this.memberProfileImage= member.getMemberProfileImage();
        this.authority = member.getAuthority();
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(memberName, password);
    }

}
