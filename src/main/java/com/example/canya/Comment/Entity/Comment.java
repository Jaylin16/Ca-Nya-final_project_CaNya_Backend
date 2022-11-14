package com.example.canya.Comment.Entity;

import com.example.canya.Board.Entity.Board;
import com.example.canya.Member.Entity.Member;
import com.example.canya.Timestamp.Timestamp;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JoinFormula;

import javax.persistence.*;

@Entity
@Getter
public class Comment extends Timestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column
    private String commentContent;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

}
