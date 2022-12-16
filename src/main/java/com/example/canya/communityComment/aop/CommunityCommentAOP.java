//package com.example.canya.communityComment.aop;
//
//import com.example.canya.community.entity.Community;
//import com.example.canya.community.repository.CommunityRepository;
//import com.example.canya.communityComment.entity.CommunityComment;
//import com.example.canya.communityComment.repository.CommunityCommentRepository;
//import com.example.canya.member.service.MemberDetailsImpl;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//
//@Aspect
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class CommunityCommentAOP {
//
//    private final CommunityCommentRepository communityCommentRepository;
//
//    @Pointcut("@annotation(com.example.canya.annotations.VerifyMemberCommunity)")
//    private void verifyMemberCommunity() {
//    }
//
//    @Around(value = "verifyMemberCommunity()&& args(.., communityId,memberDetails)", argNames = "joinPoint,memberDetails,communityId")
//    public ResponseEntity<?> verifyMemberCommunity(ProceedingJoinPoint joinPoint, MemberDetailsImpl memberDetails, Long communityId) throws Throwable {
//        Object currentMember = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        Optional<CommunityComment> optionalCommunityComment = communityCommentRepository.findById(communityId);
//
//        if (optionalCommunityComment.isEmpty()) {
//            return new ResponseEntity<>("해당 댓글이 존재하지 않습니다", HttpStatus.BAD_REQUEST);
//        }
//        CommunityComment communityComment = optionalCommunityComment.get();
//
//        if (!(communityComment.getMember().getMemberId().equals(memberDetails.getMember().getMemberId()))) {
//            return new ResponseEntity<>("댓글 작성자가 아닙니다", HttpStatus.BAD_REQUEST);
//        }
//
//        if (currentMember.equals(communityComment.getMember().getMemberName())) {
//
//            return new ResponseEntity<>(joinPoint.proceed(), HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>("invalid user", HttpStatus.BAD_REQUEST);
//        }
//    }
//
//}