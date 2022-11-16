package com.example.canya.Board.Entity;

import com.example.canya.Board.Dto.BoardRequestDto;
import com.example.canya.Comment.Entity.Comment;
import com.example.canya.Heart.Entity.Heart;
import com.example.canya.Image.Entity.Image;
import com.example.canya.Member.Entity.Member;
import com.example.canya.Rating.Dto.RatingRequestDto;
import com.example.canya.Rating.Entity.Rating;
import com.example.canya.Timestamp.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.*;

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

    @Column
    private int totalHeartCount;

    @Column
    private String highestRating;

    @Column
    private String secondHighestRating;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy= "board", cascade =  CascadeType.ALL)
    private List<Rating> ratingList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Heart> heartList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Image> imageList = new ArrayList<>();

    public Board(BoardRequestDto dto, Member member, RatingRequestDto ratingDto){
        this.member = member;
        this.boardContent = dto.getBoardContent();
        this.boardTitle = dto.getBoardTitle();
        this.address = dto.getAddress();
        this.totalHeartCount = this.heartList.size() != 0 ? this.heartList.size() : 0;
    }
    public Board(Member member){
        this.member = member;
    }
    public int getHeartCount(){
        return this.heartList.size();
    }

    public void update(BoardRequestDto dto){
        System.out.println("update called in board.java");
        this.address= dto.getAddress() != null ? dto.getAddress() : this.address;
        this.boardContent = dto.getBoardContent() != null ? dto.getBoardContent() : this.boardContent;
        this.boardTitle = dto.getBoardTitle()!= null ? dto.getBoardTitle() : this.boardTitle;
    }
    public void update(BoardRequestDto dto, String highestRate , String secondHighestRate){
        this.address= dto.getAddress() != null ? dto.getAddress() : this.address;
        this.boardContent = dto.getBoardContent() != null ? dto.getBoardContent() : this.boardContent;
        this.boardTitle = dto.getBoardTitle()!= null ? dto.getBoardTitle() : this.boardTitle;
        this.highestRating = highestRate;
        this.secondHighestRating = secondHighestRate;

    }
    public void updateHeartCount(boolean isLiked){
        if(isLiked){
            this.totalHeartCount = this.heartList.size();
        }
        this.totalHeartCount = this.totalHeartCount-1;
    }




}
