package com.example.canya.community.controller;

import com.example.canya.community.service.CommunityService;
import com.example.canya.member.service.MemberDetailsImpl;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "community (커뮤니티)")
public class CommunityController {

    private final CommunityService communityService;

    @Operation(summary = "커뮤니티 글 전체 조회", description = "커뮤니티 글 전체 조회 기능")
    @GetMapping("/get/community")
    public ResponseEntity<?> communityList() {
        return communityService.getCommunityList();
    }

    @Operation(summary = "커뮤니티 글 상세 조회", description = "커뮤니티 글 상세 조회 기능")
    @GetMapping("/community/{communityId}")
    public ResponseEntity<?> getCommuntiyDetail(@PathVariable Long communityId) {
        return communityService.getCommunityDetail(communityId);
    }

    @Operation(summary = "커뮤니티 글 삭제", description = "커뮤니티 글 삭제 기능(S3사진 포함)")
    @DeleteMapping("/auth/community/delete/{communityId}")
    public ResponseEntity<?> deleteCommunity(@PathVariable Long communityId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return communityService.deleteCommunity(communityId, memberDetails.getMember());
    }


}
