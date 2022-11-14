package com.example.canya.Member.Controller;

import com.example.canya.Board.Entity.Board;
import com.example.canya.Member.Dto.MemberRequestDto;
import com.example.canya.Member.Service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


}
