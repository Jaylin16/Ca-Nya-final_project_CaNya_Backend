package com.example.canya.Board.Controller;

import com.example.canya.Board.Dto.BoardRequestDto;
import com.example.canya.Board.Service.BoardService;
import com.example.canya.Image.Dto.ImageRequestDto;
import com.example.canya.Image.Entity.Image;
import com.example.canya.Member.Service.MemberDetailsImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
public class BoardController {


    private final BoardService boardService;


    @PutMapping("/auth/board/update/{boardId}")
    public ResponseEntity<?> editBoard(@RequestPart(value = "image", required = false) List<MultipartFile> image,
                                       @RequestParam("data") String dataList,
                                        @PathVariable Long boardId,
                                       @AuthenticationPrincipal MemberDetailsImpl memberDetails) throws IOException{
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new SimpleModule());
        BoardRequestDto dto = objectMapper.readValue(dataList, new TypeReference<>() {});
        return boardService.editBoard(dto, image, boardId,memberDetails.getMember());
    };

    @PutMapping("/auth/board/submit/{boardId}")
    public ResponseEntity<?> confirmBoard(@RequestPart(value = "image",required = false) List<MultipartFile> image,
                                         @RequestParam("data")String dataList,
                                         @PathVariable Long boardId) throws IOException {

        System.out.println("testing");
        System.out.println(dataList);
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new SimpleModule());
        BoardRequestDto dto = objectMapper.readValue(dataList, new TypeReference<>() {});

        return boardService.confirmBoard(dto, image, boardId);
    }


    @DeleteMapping("/auth/board/cancel/{boardId}")
    public ResponseEntity<?> cancelBoard(@PathVariable Long boardId , @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return boardService.cancelBoard(boardId,memberDetails.getMember());
    }

    @DeleteMapping("/auth/board/delete/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return boardService.deleteBoard(boardId, memberDetails.getMember());
    }

    @PostMapping("/auth/board/save")
    public ResponseEntity<?> saveBoard( @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        System.out.println("save method called");

        return boardService.saveBoard(memberDetails);
    }

//    @GetMapping("/get/boards")
//    public ResponseEntity<?> getBoards(@RequestBody BoardRequestDto dto){
//        return boardService.getBoards(dto);
//    }

}
