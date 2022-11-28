package com.example.canya.member.controller;

import com.example.canya.member.dto.MemberRequestDto;
import com.example.canya.member.service.MemberDetailsImpl;
import com.example.canya.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Api(tags = "member (회원가입, 로그인, 마이페이지)")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원가입", description = "회원가입 기능")
    @PostMapping("/register")
    public ResponseEntity<?> signUp(@RequestBody MemberRequestDto requestDto){
        return memberService.signUp(requestDto);
    }

    @Operation(summary = "로그인", description = "로그인 기능")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberRequestDto requestDto, HttpServletResponse response){

        return memberService.login(requestDto, response);
    }

    @Operation(summary = "아이디 중복 체크", description = "회원 가입시 사용자 아이디 중복 체크 기능")
    @GetMapping("/name/check/{memberName}")
    public ResponseEntity<?> nameCheck(@PathVariable String memberName){
        return memberService.nameCheck(memberName);

    }

    @Operation(summary = "닉네임 중복 체크", description = "회원 가입시 사용자 닉네임 중복 체크 기능")
    @GetMapping("/nickname/check/{memberNickname}")
    public ResponseEntity<?> nicknameCheck(@PathVariable String memberNickname){
        return memberService.nicknameCheck(memberNickname);

    }

    //마이페이지 내가 좋아요한 게시글 전체 조회
    @Operation(summary = "마이페이지 좋아요한 게시글", description = "마이페이지 내가 좋아요한 게시글 전체 조회 기능")
    @GetMapping("/auth/mypage/heart-boards")
    public ResponseEntity<?> getHeartBoards(@AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return memberService.getHeartBoards(memberDetails.getMember());
    }

    //마이페이지 내가 작성한 댓글 전체 조회
    @Operation(summary = "마이페이지 작성 댓글 조회", description = "마이페이지 내가 작성한 댓글 전체 조회 기능")
    @GetMapping("/auth/mypage/comments")
    public ResponseEntity<?> getMyComments(@AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return memberService.getMyComments(memberDetails.getMember());
    }

    //마이페이지 내가 작성한 게시글 전체 조회
    @Operation(summary = "마이페이지 게시글 전체 조회", description = "마이페이지 내가 작성한 게시글 전체 조회 기능")
    @GetMapping("/auth/mypage/boards")
    public ResponseEntity<?> getMyBoards(@AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return memberService.getMyBoards(memberDetails.getMember());
    }

    //마이페이지 메인 (모두 보기) 최신순 기준 3개씩만 보이도록 반환 + 유저 정보 (유저프로필 이미지, 유저가 누른 총 하트수, 유저가 작성한 게시글 수, 유저가 작성한 댓글 수)
    @Operation(summary = "마이페이지 메인 모두보기", description = "마이페이지 최신순 Top3 + 유저 정보 (프로필 이미지, 총 하트수, 총 게시글 수, 총 댓글 수)")
    @GetMapping("/auth/mypage/all")
    public ResponseEntity<?> getAllMypage(@AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return memberService.getAllMypage(memberDetails.getMember());
    }

    //마이페이지 내 프로필 사진 변경
    @Operation(summary = "마이페이지 프로필 사진 변경", description = "마이페이지 프로필 사진 변경 기능")
    @PutMapping("/auth/mypage/profile-image/update")
    public ResponseEntity<?> profileUpdate(@RequestPart(value = "image", required = false) MultipartFile image,
                                           @AuthenticationPrincipal MemberDetailsImpl memberDetails) throws IOException {
        return memberService.profileUpdate(memberDetails.getMember(), image);
    }

}
