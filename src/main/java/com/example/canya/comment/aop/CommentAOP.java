package com.example.canya.comment.aop;


import com.example.canya.comment.entity.Comment;

import com.example.canya.comment.repository.CommentRepository;
import com.example.canya.member.service.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class CommentAOP {

    private final CommentRepository commentRepository;


    @Pointcut("@annotation(com.example.canya.annotations.VerifyMemberComment)")
    private void verifyMemberComment(){}

    @Around(value = "verifyMemberComment()&& args(.., commentId,memberDetails)", argNames = "joinPoint,memberDetails,commentId")
    public ResponseEntity<?> verify(ProceedingJoinPoint joinPoint, MemberDetailsImpl memberDetails, Long commentId ) throws Throwable{
        Object currentMember = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        if(optionalComment.isEmpty()){
            return new ResponseEntity<>("댓글이 존재하지 않습니다", HttpStatus.BAD_REQUEST);
        }
        Comment comment = optionalComment.get();
        if(currentMember.equals(comment.getMember().getMemberName())){
            return new ResponseEntity<>(joinPoint.proceed(), HttpStatus.OK);
        }else{

            return new ResponseEntity<>("작성자가 다릅니다.",HttpStatus.BAD_REQUEST);
        }
    }



}
