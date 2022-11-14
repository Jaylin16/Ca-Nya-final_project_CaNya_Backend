package com.example.canya.Member.Entity;

import com.example.canya.Board.Entity.Board;
import com.example.canya.Comment.Entity.Comment;
import com.example.canya.Member.Dto.MemberRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column
    private String memberName;

    @Column
    private String memberNickname;

    @Column
    private String password;

    @Column
    private String memberProfileImage;

    @OneToMany(mappedBy ="member", cascade = CascadeType.REMOVE)
    private List<Board> Board = new ArrayList<>();

    @OneToMany(mappedBy ="member", cascade = CascadeType.REMOVE)
    private List<Comment> Comment = new ArrayList<>();

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private Authority authority;

    public Member (MemberRequestDto dto){
        this.memberName = dto.getMemberName();
        this.memberNickname = dto.getMemberNickname();
        this.password = dto.getPassword();
    }

}
