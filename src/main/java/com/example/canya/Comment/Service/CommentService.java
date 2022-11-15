package com.example.canya.Comment.Service;

import com.example.canya.Board.Entity.Board;
import com.example.canya.Board.Repository.BoardRepository;
import com.example.canya.Comment.Dto.CommentRequestDto;
import com.example.canya.Comment.Dto.CommentResponseDto;
import com.example.canya.Comment.Entity.Comment;
import com.example.canya.Comment.Repository.CommentRepository;
import com.example.canya.Member.Entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;


    //댓글 생성.
    public ResponseEntity<?> createComment(Long boardId, CommentRequestDto commentRequestDto, Member member) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        Comment commentContent = new Comment(commentRequestDto, board, member);
        commentRepository.save(commentContent);

        return new ResponseEntity<>("댓글 생성이 완료되었습니다.", HttpStatus.OK);
    }

    //댓글 전체 불러오기.
    public ResponseEntity<?> getCommentList() {
        List<Comment> comments = commentRepository.findAll();

        List<CommentResponseDto> commentList = new ArrayList<>();

        for (Comment comment : comments) {
            CommentResponseDto commentResponseDto = new CommentResponseDto(comment);

            commentList.add(commentResponseDto);
        }
        return new ResponseEntity<>(commentList, HttpStatus.OK);
    }

    //댓글 삭제
    @Transactional
    public ResponseEntity<?> deleteComment(Long commentId, Member member) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if(comment.isEmpty()){
            return new ResponseEntity<>("해당 댓글이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        if(!(Objects.equals(comment.get().getMember().getMemberId(), member.getMemberId()))){
            return new ResponseEntity<>("본인이 작성한 댓글만 삭제 가능합니다.", HttpStatus.BAD_REQUEST);
        }

        commentRepository.deleteById(commentId);

        return new ResponseEntity<>("삭제가 완료되었습니다.",HttpStatus.OK);
    }

    //댓글 수정
    @Transactional
    public ResponseEntity<?> commentUpdate(Long commentId, CommentRequestDto commentRequestDto, Member member) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isEmpty()) {
            return new ResponseEntity<>("해당 댓글이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        if (!(Objects.equals(comment.get().getMember().getMemberId(), member.getMemberId()))) {
            return new ResponseEntity<>("본인이 작성한 댓글만 수정 가능합니다.", HttpStatus.BAD_REQUEST);
        }

        comment.get().update(commentRequestDto);

        return new ResponseEntity<>("수정 성공!", HttpStatus.OK);
    }
}
