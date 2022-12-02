package com.example.canya.community.controller;

import com.example.canya.community.dto.CommunityRequestDto;
import com.example.canya.community.service.CommunityService;
import com.example.canya.member.service.MemberDetailsImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    // 커뮤니티 글 작성
    @PostMapping("/auth/save/community")
    public ResponseEntity<?> saveCommunity(
            @ModelAttribute(value = "data")String dataList,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestPart(value = "image", required = false)MultipartFile image
            ) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new SimpleModule());

        CommunityRequestDto communityRequestDto = objectMapper.readValue(dataList, new TypeReference<>() {});

        return communityService.saveCommunity(communityRequestDto, memberDetails.getMember(), image);
    }

    // 커뮤니티 글 수정
    @PutMapping("/auth/update/community/{communityId}")
    public ResponseEntity<?> updateCommunity(
            @PathVariable Long communityId,
            @RequestParam(value = "data") String dataList,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestPart(value = "image", required = false)MultipartFile image
            ) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new SimpleModule());

        CommunityRequestDto communityRequestDto = objectMapper.readValue(dataList, new TypeReference<>() {});

        return communityService.updateCommunity(communityId, communityRequestDto, memberDetails, image);
    }

}
