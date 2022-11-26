package com.example.canya.Comment.Entity;

import com.example.canya.Board.Entity.Board;
import com.example.canya.Comment.Dto.CommentRequestDto;
import com.example.canya.Member.Entity.Member;
import com.example.canya.Timestamp.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
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

    public Comment(CommentRequestDto commentRequestDto, Board board, Member member) {
        this.commentContent = commentRequestDto.getCommentContent();
        this.board = board;
        this.member = member;
    }

    public void update(CommentRequestDto commentRequestDto) {
        this.commentContent = commentRequestDto.getCommentContent();
    }
}
