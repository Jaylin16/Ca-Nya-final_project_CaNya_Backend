package com.example.canya.communityComment.controller;

import com.example.canya.annotations.VerifyMemberCommunityComment;
import com.example.canya.communityComment.dto.CommunityCommentRequestDto;
import com.example.canya.communityComment.service.CommunityCommentService;
import com.example.canya.member.service.MemberDetailsImpl;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Api(tags = "communityComment (커뮤니티 댓글)")
public class CommunityCommentController {

    private final CommunityCommentService communityCommentService;

    @Operation(summary = "커뮤니티 댓글 생성", description = "특정 커뮤니티 글에 댓글 생성 기능")
    @PostMapping("/auth/communityComment/{communityId}/create")
    public ResponseEntity<?> createCommunityComment(@PathVariable Long communityId, @RequestBody CommunityCommentRequestDto communityCommentRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return communityCommentService.createCommunityComment(communityId, communityCommentRequestDto, memberDetails.getMember());
    }

    @Operation(summary = "커뮤니티 글별 댓글 조회", description = "커뮤니티 게시물 별 댓글 전체 조회 기능")
    @GetMapping("/get/{communityId}/communityComments")
    public ResponseEntity<?> communityCommentList(@PathVariable Long communityId) {
        return communityCommentService.getcommunityCommentList(communityId);
    }

    @Operation(summary = "커뮤니티 글별 댓글 수정", description = "커뮤니티 게시물 별 댓글 수정 기능")
    @VerifyMemberCommunityComment
    @PutMapping("/auth/communityComment/{communityCommentId}/update")
    public ResponseEntity<?> updateCommunityComment(@RequestBody CommunityCommentRequestDto communityCommentRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails, @PathVariable Long communityCommentId) {
        return communityCommentService.updateCommunityComment(communityCommentId, communityCommentRequestDto, memberDetails);
    }

    @Operation(summary = "커뮤니티 글별 댓글 삭제", description = "커뮤니티 게시물 별 댓글 삭제 기능")
    @VerifyMemberCommunityComment
    @DeleteMapping("/auth/communityComment/{communityCommentId}/delete")
    public ResponseEntity<?> deleteCommunityComment(@PathVariable Long communityCommentId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return communityCommentService.deleteCommunityComment(communityCommentId, memberDetails.getMember());
    }
}
