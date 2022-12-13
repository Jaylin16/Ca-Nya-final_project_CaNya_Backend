package com.example.canya.board.aop;

import com.example.canya.board.entity.Board;
import com.example.canya.board.repository.BoardRepository;
import com.example.canya.board.service.BoardService;
import com.example.canya.image.entity.Image;
import com.example.canya.image.repository.ImageRepository;
import com.example.canya.member.entity.Member;
import com.example.canya.member.service.MemberDetailsImpl;
import com.example.canya.s3.S3Uploader;
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
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class BoardAOP {

    private final BoardRepository boardRepository;
    private final S3Uploader s3Uploader;
    private final ImageRepository imageRepository;

    @Pointcut("@annotation(com.example.canya.annotations.VerifyMemberBoard)")
    private void verifyMember(){}

    @Pointcut("@annotation(com.example.canya.annotations.AddImage)")
    private void addImage(){}

//    @Around(value = "verifyMember()&& args(.., boardId,memberDetails)", argNames = "joinPoint,memberDetails,boardId")
//    public ResponseEntity<?> verify(ProceedingJoinPoint joinPoint, MemberDetailsImpl memberDetails, Long boardId ) throws Throwable{
//        Object currentMember = SecurityContextHolder.getContext().getAuthentication().getName();
//        Optional<Board> optionalBoard = boardRepository.findById(boardId);
//
//        if(optionalBoard.isEmpty()){
//            return new ResponseEntity<>("게시글이 존재하지 않습니다",HttpStatus.BAD_REQUEST);
//        }
//        Board board = optionalBoard.get();
//        if(currentMember.equals(board.getMember().getMemberName())){
//
//            return new ResponseEntity<>(joinPoint.proceed(), HttpStatus.OK);
//        }else{
//
//            return new ResponseEntity<>("invalid user",HttpStatus.BAD_REQUEST);
//        }
//    }

    @Around(value = "addImage() && args(..,images,boardId)")
    public Object handleImage(ProceedingJoinPoint joinPoint,  List<MultipartFile> images,Long boardId) throws Throwable {
        Board board = boardRepository.findById(boardId).orElseThrow();
        List<Image> imageList = imageRepository.findAllByBoard(board);
        List<String> imageUrlList = new ArrayList<>();

        for (Image image : imageList) {
            imageUrlList.add(image.getImageUrl());

        }
        Member member = board.getMember();
        if (images != null) {
            for (MultipartFile image : images) {
                imageRepository.save(new Image(board, s3Uploader.upload(image, "boardImage"), member));
            }
            for (String url : imageUrlList) {
                String target = "boardImage" + url.substring(url.lastIndexOf("/"));
                s3Uploader.deleteFile(target);
            }
        }
        else{
            String DEFAULT_THUMBNAIL = "https://assets.website-files.com/5ee732bebd9839b494ff27cd/5ef0a6c746931523ace53017_Starbucks.jpg";
            imageRepository.save(new Image(board, DEFAULT_THUMBNAIL, member));
        }

        return joinPoint.proceed();
    }
}
