package com.example.canya.heart.entity;
import com.example.canya.board.entity.Board;
import com.example.canya.member.entity.Member;
import com.example.canya.timestamp.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Heart extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long heartId;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Heart(Member member, Board board) {
        this.member = member;
        this.board = board;
    }
}
