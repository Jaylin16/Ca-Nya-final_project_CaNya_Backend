package com.example.canya.heart.controller;

import com.example.canya.heart.service.HeartService;
import com.example.canya.member.service.MemberDetailsImpl;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "heart (좋아요)")
public class HeartController {
    private final HeartService heartService;

    @Operation(summary = "좋아요 생성/삭제", description = "좋아요 기능")
    @PostMapping("/auth/{boardId}/heart/create")
    public ResponseEntity<?> heartCreate (@PathVariable Long boardId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return heartService.heartCreate(boardId, memberDetails.getMember());
    }
}
