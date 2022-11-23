package com.example.canya.Board.Controller;

import com.example.canya.Board.Dto.BoardRequestDto;
import com.example.canya.Board.Dto.UpdateUrlDto;
import com.example.canya.Board.Service.BoardService;
import com.example.canya.Member.Service.MemberDetailsImpl;
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
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Api(tags = "Board (메인 페이지, 게시글)")
public class BoardController {
    private final BoardService boardService;

    //메인 페이지 조회
    @Operation(summary = "메인 페이지", description = "메인 페이지 조회 기능")
    @GetMapping("/board/main")
    public ResponseEntity<?> getMainBoards(){

        return boardService.getBoards();
    }
    //메인 페이지 카테고리별 조회
    @Operation(summary = "메인 카테고리별 조회", description = "메인 페이지 카테고리별 조회 기능")
    @GetMapping("/board/main/{keyword}")
    public ResponseEntity<?> getMainCategory(@PathVariable String keyword,
                                             @RequestParam(value = "page",required = false) Integer page,
                                             @RequestParam(value = "size" , required = false) Integer size){
        Integer pageTemp = page -1;

        return boardService.getMainCategory(keyword,pageTemp, size);
    }

    //게시물 상세 조회
    @Operation(summary = "게시물 상세 조회", description = "각 게시된 글 조회 기능")
    @GetMapping("/board/{boardId}")
    public ResponseEntity<?> getBoardDetail(@PathVariable Long boardId){

        return boardService.getBoardDetail(boardId);
    }

    //게시물 검색
    @GetMapping("/board/search/{category}/{keyword}")
    public ResponseEntity<?> searchBoard(@PathVariable String category,
                                         @PathVariable String keyword,
                                         @RequestParam(required = false) Integer page,
                                         @RequestParam(required = false) Integer size){

        Integer pageTemp = page -1;
        return boardService.searchBoard(category,keyword,pageTemp,size);
    }

    //게시물 등록 시작
    @Operation(summary = "게시물 ID 생성", description = "게시물 작성 페이지 진입시 boardId 부여")
    @PostMapping("/auth/board/save")
    public ResponseEntity<?> saveBoard( @AuthenticationPrincipal MemberDetailsImpl memberDetails){

        return boardService.saveBoard(memberDetails);
    }

    //게시물 수정 완료
    // 이 부분에서 수정을 할때, 프론트에서 boardContent / boardTitle 은 무조건 받아야함.
    // 기존 값이라도 오게 해야함. 아무것도 없는 공란이라면 Request 금지.
    // 만약 아무런 값도 안오게 한다면 boardService 에 추가 method 생성 하고 할순있지만...
    @Operation(summary = "게시물 편집", description = "게시글 수정 기능")
    @PutMapping("/auth/board/update/{boardId}")
    public ResponseEntity<?> editBoard(@RequestPart(value = "images", required = false) List<MultipartFile> images,
                                       @RequestParam(value = "data", required = false) String dataList,
                                       @PathVariable Long boardId,
                                       @RequestParam(value = "url", required = false) String urlList,
                                       @AuthenticationPrincipal MemberDetailsImpl memberDetails) throws IOException{

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new SimpleModule());
        BoardRequestDto dto = objectMapper.readValue(dataList, new TypeReference<>() {});
        if(urlList != null){
            UpdateUrlDto urlDto = objectMapper.readValue(urlList, new TypeReference<>(){});
            return boardService.editBoard(dto, images, boardId,memberDetails.getMember(),urlDto.getUrlList());
        }

        UpdateUrlDto urlDto = new UpdateUrlDto();

        return boardService.editBoard(dto, images, boardId,memberDetails.getMember(),urlDto.getUrlList());
    };

    //게시물 등록 완료
    @Operation(summary = "게시물 등록", description = "기존 boardId 부여된 게시글 작성 완료 기능")
    @PutMapping("/auth/board/submit/{boardId}")
    public ResponseEntity<?> confirmBoard(@RequestPart(value = "image",required = false) List<MultipartFile> image,
                                          @RequestParam("data")String dataList,
                                          @PathVariable Long boardId) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new SimpleModule());


        BoardRequestDto dto = objectMapper.readValue(dataList, new TypeReference<>() {});

        return boardService.confirmBoard(dto, image, boardId);
    }

    //게시물 삭제
    @Operation(summary = "게시물 삭제", description = "작성된 게시글 삭제 기능")
    @DeleteMapping("/auth/board/delete/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId, @AuthenticationPrincipal MemberDetailsImpl memberDetails){

        return boardService.deleteBoard(boardId, memberDetails.getMember());
    }
}
