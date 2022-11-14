package com.example.canya.Comment.Controller;

import com.example.canya.Comment.Dto.CommentRequestDto;
import com.example.canya.Comment.Service.CommentService;
import com.example.canya.Member.Service.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //댓글 생성.
    @PostMapping("/auth/comment/{boardId}/create")
    public ResponseEntity<?> createComment (@PathVariable Long boardId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return commentService.createComment(boardId, commentRequestDto, memberDetails.getMember());
    }

    //댓글 전체 조회
    @GetMapping("/get/comments")
    public ResponseEntity<?> commentList () {
        return commentService.getCommentList();
    }

    //댓글 삭제
    @DeleteMapping("/auth/comment/{commentId}/delete")
    public ResponseEntity<?> commentDelete(@PathVariable Long commentId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return commentService.deleteComment(commentId, memberDetails.getMember());
    }


    //댓글 수정
    @PutMapping("/auth/comment/{commentId}/update")
    public ResponseEntity<?> commentUpdate(@PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return commentService.commentUpdate(commentId, commentRequestDto, memberDetails.getMember());
    }


}
