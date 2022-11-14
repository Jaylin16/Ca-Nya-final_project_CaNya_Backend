package com.example.canya.Heart.Controller;

import com.example.canya.Heart.Service.HeartService;
import com.example.canya.Member.Service.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HeartController {

    private final HeartService heartService;

    @PostMapping("/auth/{boardId}/heart/create")
    public ResponseEntity<?> heartCreate (@PathVariable Long boardId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return heartService.heartCreate(boardId, memberDetails.getMember());
    }
}
