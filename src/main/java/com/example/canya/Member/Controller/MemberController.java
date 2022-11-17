package com.example.canya.Member.Controller;

import com.example.canya.Member.Dto.MemberRequestDto;
import com.example.canya.Member.Service.MemberDetailsImpl;
import com.example.canya.Member.Service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<?> signUp(@RequestBody MemberRequestDto requestDto){
        return memberService.signUp(requestDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberRequestDto requestDto, HttpServletResponse response){

        return memberService.login(requestDto, response);
    }

    @GetMapping("/name/check/{memberName}")
    public ResponseEntity<?> nameCheck(@PathVariable String memberName){
        return memberService.nameCheck(memberName);

    }

    @GetMapping("/nickname/check/{memberNickname}")
    public ResponseEntity<?> nicknameCheck(@PathVariable String memberNickname){
        return memberService.nicknameCheck(memberNickname);

    }

    //마이페이지 내가 좋아요한 게시글 전체 조회
    @GetMapping("/auth/mypage/heart-boards")
    public ResponseEntity<?> getHeartBoards(@AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return memberService.getHeartBoards(memberDetails.getMember());
    }

    //마이페이지 내가 작성한 댓글 전체 조회
    @GetMapping("/auth/mypage/comments")
    public ResponseEntity<?> getMyComments(@AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return memberService.getMyComments(memberDetails.getMember());
    }

    //마이페이지 내가 작성한 게시글 전체 조회
    @GetMapping("/auth/mypage/boards")
    public ResponseEntity<?> getMyBoards(@AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return memberService.getMyBoards(memberDetails.getMember());
    }

    //마이페이지 메인 (모두 보기) 최신순 기준 3개씩만 보이도록 반환
    @GetMapping("/auth/mypage/all")
    public ResponseEntity<?> getAllMypage(@AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return memberService.getAllMypage(memberDetails.getMember());
    }

    //마이페이지 내 프로필 사진 변경
    @PutMapping("/auth/mypage/profile-image/update")
    public ResponseEntity<?> profileUpdate(@RequestPart(value = "image", required = false) MultipartFile image,
                                           @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return memberService.profileUpdate(memberDetails.getMember());
    }

}
