package com.example.canya.Board.Entity;

import com.example.canya.Board.Dto.BoardRequestDto;
import com.example.canya.Comment.Entity.Comment;
import com.example.canya.Heart.Entity.Heart;
import com.example.canya.Image.Entity.Image;
import com.example.canya.Member.Entity.Member;
import com.example.canya.Timestamp.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Board extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column
    private String boardTitle;

    @Column
    private String boardContent;

    @Column
    private String address;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Heart> heartList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Image> imageList = new ArrayList<>();
    public Board(BoardRequestDto dto, Member member){
        this.member = member;
        this.boardContent = dto.getBoardContent();
        this.boardTitle = dto.getBoardTitle();
        this.address = dto.getAddress();
    }
    public Board(Member member){
        this.member = member;
    }

    public void update(BoardRequestDto dto){
        this.address= dto.getAddress() != null ? dto.getAddress() : this.address;
        this.boardContent = dto.getBoardContent() != null ? dto.getBoardContent() : this.boardContent;
        this.boardTitle = dto.getBoardTitle()!= null ? dto.getBoardTitle() : this.boardTitle;
    }




}
