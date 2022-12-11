package com.example.canya.community.aop;

import com.example.canya.board.entity.Board;
import com.example.canya.community.entity.Community;
import com.example.canya.community.repository.CommunityRepository;
import com.example.canya.member.service.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class CommunityAOP {

    private final CommunityRepository communityRepository;

    @Pointcut("@annotation(com.example.canya.annotations.VerifyMemberCommunity)")
    private void verifyMemberCommunity(){}

    @Around(value = "verifyMemberCommunity()&& args(.., communityId,memberDetails)", argNames = "joinPoint,memberDetails,communityId")
    public ResponseEntity<?> verifyCommunity(ProceedingJoinPoint joinPoint, MemberDetailsImpl memberDetails, Long communityId ) throws Throwable{
        Object currentMember = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Community> optionalCommunity = communityRepository.findById(communityId);

        if(optionalCommunity.isEmpty()){
            return new ResponseEntity<>("게시글이 존재하지 않습니다", HttpStatus.BAD_REQUEST);
        }

        Community community = optionalCommunity.get();
        if(currentMember.equals(community.getMember().getMemberName())){

            return new ResponseEntity<>(joinPoint.proceed(), HttpStatus.OK);
        }else{

            return new ResponseEntity<>("invalid user",HttpStatus.BAD_REQUEST);
        }
    }

}
