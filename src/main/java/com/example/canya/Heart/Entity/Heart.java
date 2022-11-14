package com.example.canya.Heart.Entity;

import com.example.canya.Board.Entity.Board;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Entity
public class Heart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long heartId;

    @Column
    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

}
