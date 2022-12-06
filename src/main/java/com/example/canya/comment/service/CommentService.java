package com.example.canya.comment.service;

import com.example.canya.board.entity.Board;
import com.example.canya.board.repository.BoardRepository;
import com.example.canya.comment.dto.CommentRequestDto;
import com.example.canya.comment.dto.CommentResponseDto;
import com.example.canya.comment.entity.Comment;
import com.example.canya.comment.repository.CommentRepository;
import com.example.canya.member.entity.Member;
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

    public ResponseEntity<?> createComment(Long boardId, CommentRequestDto commentRequestDto, Member member) {
        Optional<Board> board = boardRepository.findById(boardId);

        if(board.isEmpty()) {
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        Comment commentContent = new Comment(commentRequestDto, board.get(), member);
        commentRepository.save(commentContent);

        return new ResponseEntity<>("댓글 생성이 완료되었습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> getCommentList() {
        List<Comment> comments = commentRepository.findAll();

        List<CommentResponseDto> commentList = new ArrayList<>();

        for (Comment comment : comments) {
            CommentResponseDto commentResponseDto = new CommentResponseDto(comment);

            commentList.add(commentResponseDto);
        }
        return new ResponseEntity<>(commentList, HttpStatus.OK);
    }



    public ResponseEntity<?> getBoardCommentList(Long boardId) {
        List<Comment> comments = commentRepository.findAllByBoard_BoardId(boardId);
        if (comments == null) {
            return new ResponseEntity<>("본 게시글에는 댓글이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        List<com.example.canya.comment.dto.CommentResponseDto> boardCommentList = new ArrayList<>();
        for(Comment commentList : comments) {
            com.example.canya.comment.dto.CommentResponseDto commentResponseDto = new com.example.canya.comment.dto.CommentResponseDto(commentList);

            boardCommentList.add(commentResponseDto);
        }


        return new ResponseEntity<>(boardCommentList, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> commentUpdate(Long commentId, CommentRequestDto commentRequestDto, Member member) {
        Optional<Comment> comment = commentRepository.findById(commentId);
//        if (comment.isEmpty()) {
//            return new ResponseEntity<>("해당 댓글이 없습니다.", HttpStatus.BAD_REQUEST);
//        }
//
//        if (!(Objects.equals(comment.get().getMember().getMemberId(), member.getMemberId()))) {
//            return new ResponseEntity<>("본인이 작성한 댓글만 수정 가능합니다.", HttpStatus.BAD_REQUEST);
//        }

        comment.get().update(commentRequestDto);

        return new ResponseEntity<>("수정 성공!", HttpStatus.OK);
    }

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


}