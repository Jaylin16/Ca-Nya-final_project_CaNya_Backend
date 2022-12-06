package com.example.canya.member.entity;

import com.example.canya.board.entity.Board;
import com.example.canya.comment.entity.Comment;
import com.example.canya.heart.entity.Heart;
import com.example.canya.member.dto.MemberRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String status;

    @Column
    private String memberNickname;

    @Column
    private String password;

    @Column
    private String memberProfileImage;

    @OneToMany(mappedBy ="member", cascade = CascadeType.REMOVE)
    private List<com.example.canya.board.entity.Board> Board = new ArrayList<>();

    @OneToMany(mappedBy ="member", cascade = CascadeType.REMOVE)
    private List<Comment> Comment = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Heart> Heart = new ArrayList<>();

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private Authority authority;

    public Member (MemberRequestDto dto){
        this.memberName = dto.getMemberName();
        this.memberNickname = dto.getMemberNickname();
        this.password = dto.getPassword();
    }
    public String getStatus(){
        int boardNum = this.Board.size();
        System.out.println("boardNum = " + boardNum);
        if(this.Board.size() == 0 ){
            System.out.println("it should be tall");
            this.status = "tall";
            return this.status;
        }
        if(this.Board.size() == 1){
            this.status = "grande";
            return this.status;
        }
        this.status = "venti";
        return this.status;
    }

    public void update (String imageUrl) {
        this.memberProfileImage = imageUrl;
    }

}