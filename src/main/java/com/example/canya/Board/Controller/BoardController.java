package com.example.canya.Board.Controller;

import com.example.canya.Board.Dto.BoardRequestDto;
import com.example.canya.Board.Dto.CoffeePick;
import com.example.canya.Board.Dto.UpdateUrl;
import com.example.canya.Board.Service.BoardService;
import com.example.canya.Member.Service.MemberDetailsImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    //메인 페이지 조회
    @GetMapping("/board/main")
    public ResponseEntity<?> getMainBoards(){

        return boardService.getBoards();
    }
    //메인 페이지 카테고리별 조회
    @GetMapping("/board/main/{keyword}")
    public ResponseEntity<?> getMainCategory(@PathVariable String keyword){

        return boardService.getMainCategory(keyword);
    }

    //게시물 상세 조회
    @GetMapping("/board/{boardId}")
    public ResponseEntity<?> getBoardDetail(@PathVariable Long boardId){

        return boardService.getBoardDetail(boardId);
    }

//    @GetMapping("/infinite-scroll")
//    public Map<String, List<CoffeePick>> getCategoryListScroll(@RequestParam(required = false) Integer page,
//                                                               @RequestParam(required = false) Integer size,
//                                                               @RequestParam(required = false) String sortBy,
//                                                               @RequestParam(required = false) Boolean isAsc){
//        if(isNotNullParam ( page, size , sortBy , isAsc)){
//            page = -1;
//            return boardService.getCategoryListScroll(page,size, sortBy, isAsc);
//        }else{
//            throw new RuntimeException("해당 페이지가 존재하지 않습니다.");
//        }
//    }
    //게시물 등록 시작
    @PostMapping("/auth/board/save")
    public ResponseEntity<?> saveBoard( @AuthenticationPrincipal MemberDetailsImpl memberDetails){

        return boardService.saveBoard(memberDetails);
    }

    //게시물 수정 완료
    @PutMapping("/auth/board/update/{boardId}")
    public ResponseEntity<?> editBoard(@RequestPart(value = "images", required = false) List<MultipartFile> images,
                                       @RequestParam("data") String dataList,
                                       @PathVariable Long boardId,
                                       @RequestParam(value = "url", required = false) String urlList,
                                       @AuthenticationPrincipal MemberDetailsImpl memberDetails) throws IOException{


        ObjectMapper objectMapper = new ObjectMapper().registerModule(new SimpleModule());
        BoardRequestDto dto = objectMapper.readValue(dataList, new TypeReference<>() {});
        if(urlList != null){
            UpdateUrl urlDto = objectMapper.readValue(urlList, new TypeReference<>(){});
            System.out.println("edit controller line 79 " + Arrays.toString(urlDto.getUrlList()));
            return boardService.editBoard(dto, images, boardId,memberDetails.getMember(),urlDto.getUrlList());

        }

        UpdateUrl urlDto = new UpdateUrl();


        return boardService.editBoard(dto, images, boardId,memberDetails.getMember(),urlDto.getUrlList());
    };

    //게시물 등록 완료
    @PutMapping("/auth/board/submit/{boardId}")
    public ResponseEntity<?> confirmBoard(@RequestPart(value = "image",required = false) List<MultipartFile> image,
                                          @RequestParam("data")String dataList,
                                          @PathVariable Long boardId) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new SimpleModule());


        BoardRequestDto dto = objectMapper.readValue(dataList, new TypeReference<>() {});

        return boardService.confirmBoard(dto, image, boardId);
    }

    //게시물 등록 중 취소
    @DeleteMapping("/auth/board/cancel/{boardId}")
    public ResponseEntity<?> cancelBoard(@PathVariable Long boardId , @AuthenticationPrincipal MemberDetailsImpl memberDetails){

        return boardService.cancelBoard(boardId,memberDetails.getMember());
    }

    //게시물 삭제
    @DeleteMapping("/auth/board/delete/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId, @AuthenticationPrincipal MemberDetailsImpl memberDetails){

        return boardService.deleteBoard(boardId, memberDetails.getMember());
    }
}
