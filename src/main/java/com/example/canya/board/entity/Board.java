package com.example.canya.board.entity;

import com.example.canya.board.dto.BoardRequestDto;
import com.example.canya.comment.entity.Comment;
import com.example.canya.heart.entity.Heart;
import com.example.canya.image.entity.Image;
import com.example.canya.member.entity.Member;
import com.example.canya.rating.entity.Rating;
import com.example.canya.timestamp.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @Column
    private String addressId;
    @Column
    private Boolean isReady;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratingList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Heart> heartList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Image> imageList = new ArrayList<>();

    public Board(Member member) {
        this.member = member;
    }

    public int getHeartCount() {
        return this.heartList.size();
    }

    public void update(BoardRequestDto dto) {
        this.address = dto.getAddress();
        this.boardContent = dto.getBoardContent();
        this.boardTitle = dto.getBoardTitle();
        this.isReady = true;
        this.addressId = dto.getAddressId();
    }

    public void update(BoardRequestDto dto, List<String> highestRatings) {
        this.address = dto.getAddress();
        this.addressId = dto.getAddressId();
        this.boardContent = dto.getBoardContent();
        this.boardTitle = dto.getBoardTitle();
        this.highestRating = highestRatings.get(0);
        this.secondHighestRating = highestRatings.get(1);
        this.isReady = true;
    }

    public void updateHeartCount(boolean isLiked) {
        if (isLiked) {
            this.totalHeartCount = this.heartList.size();
        }
        if (!isLiked) {
            this.totalHeartCount = this.totalHeartCount - 1;
        }
    }
}
