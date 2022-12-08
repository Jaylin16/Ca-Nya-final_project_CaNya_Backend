package com.example.canya.comment.controller;

import com.example.canya.annotations.VerifyMemberComment;
import com.example.canya.comment.dto.CommentRequestDto;
import com.example.canya.comment.service.CommentService;
import com.example.canya.member.service.MemberDetailsImpl;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Api(tags = "comment (댓글)")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 생성", description = "특정 게시물에 댓글 생성 기능")
    @PostMapping("/auth/comment/{boardId}/create")
    public ResponseEntity<?> createComment (@PathVariable Long boardId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return commentService.createComment(boardId, commentRequestDto, memberDetails.getMember());
    }

    @Operation(summary = "댓글 전체 조회", description = "작성된 댓글 전체 조회 기능")
    @GetMapping("/get/comments")
    public ResponseEntity<?> commentList () {
        return commentService.getCommentList();
    }


    @Operation(summary = "댓글 삭제", description = "댓글 삭제 기능")
    @VerifyMemberComment
    @DeleteMapping("/auth/comment/{commentId}/delete")
    public ResponseEntity<?> commentDelete(@PathVariable Long commentId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return commentService.deleteComment(commentId, memberDetails.getMember());
    }

    @Operation(summary = "게시물 댓글 조회", description = "게시물 별 댓글 전체 조회 기능")
    @GetMapping("/get/{boardId}/comments")
    public ResponseEntity<?> boardCommentList (@PathVariable Long boardId) {
        return commentService.getBoardCommentList(boardId);

    }

    @Operation(summary = "댓글 수정", description = "댓글 수정 기능")
    @VerifyMemberComment
    @PutMapping("/auth/comment/{commentId}/update")
    public ResponseEntity<?> commentUpdate(@RequestBody CommentRequestDto commentRequestDto, @PathVariable Long commentId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return commentService.commentUpdate(commentId, commentRequestDto, memberDetails.getMember());
    }




}
