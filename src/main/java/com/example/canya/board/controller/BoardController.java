package com.example.canya.board.controller;

import com.example.canya.annotations.Timer;
import com.example.canya.annotations.VerifyMember;
import com.example.canya.board.dto.BoardRequestDto;
import com.example.canya.board.dto.UpdateUrlDto;
import com.example.canya.board.service.BoardService;
import com.example.canya.member.service.MemberDetailsImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Api(tags = "board (메인 페이지, 게시글)")
public class BoardController {
    private final BoardService boardService;

    @Timer
    @Operation(summary = "메인 페이지", description = "메인 페이지 조회 기능")
    @GetMapping("/board/main")
    public ResponseEntity<?> getMainBoards(){

        return boardService.getBoards();
    }

    @Timer
    @Operation(summary = "게시물 상세 조회", description = "각 게시된 글 조회 기능")

    @GetMapping("/board/{boardId}")
    public ResponseEntity<?> getBoardDetail(@PathVariable Long boardId, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        if(memberDetails == null){
            return boardService.getBoardDetailWithoutLogin(boardId);
        }
        return boardService.getBoardDetail(boardId, memberDetails);
    }


    @Operation(summary = "게시물 ID 생성", description = "게시물 작성 페이지 진입시 boardId 부여")
    @PostMapping("/auth/board/save")
    public ResponseEntity<?> saveBoard( @AuthenticationPrincipal MemberDetailsImpl memberDetails){

        return boardService.saveBoard(memberDetails.getMember());
    }

    @Operation(summary = "게시물 편집", description = "게시글 수정 기능")
    @VerifyMember
    @PutMapping("/auth/board/update/{boardId}")
    public ResponseEntity<?> editBoard(@RequestPart(value = "images", required = false) List<MultipartFile> images,
                                       @RequestParam(value = "data", required = false) String dataList,
                                       @RequestParam(value = "url", required = false) String urlList,
                                       @PathVariable Long boardId,
                                       @AuthenticationPrincipal MemberDetailsImpl memberDetails) throws IOException{

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new SimpleModule());
        BoardRequestDto dto = objectMapper.readValue(dataList, new TypeReference<>() {});

        if(urlList.length()!=0){
            UpdateUrlDto urlDto = objectMapper.readValue(urlList, new TypeReference<>(){});
            return boardService.editBoard(dto, memberDetails.getMember(),urlDto.getUrlList(),images, boardId);
        }

        UpdateUrlDto urlDto = new UpdateUrlDto();

        return boardService.editBoard(dto,memberDetails.getMember(),urlDto.getUrlList(),images, boardId);
    };

    @Operation(summary = "게시물 등록", description = "기존 boardId 부여된 게시글 작성 완료 기능")
    @PutMapping("/auth/board/submit/{boardId}")
    public ResponseEntity<?> confirmBoard(@RequestPart(value = "image",required = false) List<MultipartFile> image,
                                          @RequestParam("data")String dataList,
                                          @PathVariable Long boardId) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new SimpleModule());
        BoardRequestDto dto = objectMapper.readValue(dataList, new TypeReference<>() {});

        return boardService.confirmBoard(dto, image, boardId);
    }

    @Operation(summary = "게시물 삭제", description = "작성된 게시글 삭제 기능")
    @VerifyMember
    @DeleteMapping("/auth/board/delete/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId, @AuthenticationPrincipal MemberDetailsImpl memberDetails){

        return boardService.deleteBoard(boardId);
    }
}