//package com.example.canya.chat.controller;
//
//import com.example.canya.chat.dto.ChatRoomRequestDto;
//import com.example.canya.chat.repository.ChatRoomRepository;
//import com.example.canya.chat.service.ChatService;
//import com.example.canya.member.service.MemberDetailsImpl;
//import io.swagger.v3.oas.annotations.Operation;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.coyote.Response;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@Slf4j
//@RequiredArgsConstructor
//@RequestMapping("/chat/auth")
//public class ChatRoomController {
//
//    private final ChatService chatService;
//    @GetMapping("/chatroom")    //  채팅방 목록 조회
//    public ResponseEntity<?> getChatRoom(@AuthenticationPrincipal MemberDetailsImpl userDetails){   // todo : 마지막 채팅 1줄도 같이 포함해서 반환 , 채팅방 이름이 대화 상대일 경우 로직
//
//        log.info("member [ {} ] called getChatRoom", userDetails.getMember().getMemberId());
//        List chatRoom = chatService.getChatRoom(userDetails.getMember().getMemberId());
//        if(chatRoom==null){
//            return ResponseEntity.ok("참여하고 있는 채팅방이 없습니다.");   // todo : 성호님한테 그냥 data 에 null 넘기면 안되는지 물어보기, 또는 빈 리스트 ?
//        }
//        return ResponseEntity.ok(chatRoom);
//    }
//
//
//    @GetMapping("/chatroom/{id}/history")   // 채팅 기록 조회
//    public ResponseEntity<?> getChatRoomHistory(@PathVariable String roomId, @AuthenticationPrincipal MemberDetailsImpl userDetails){
//        return chatService.getChatRoomHistory(roomId, userDetails.getMember());
//    }
//
//    @PostMapping("/chatroom")   // 채팅방 생성
//    public ResponseEntity<?> createChatRoom(@RequestBody ChatRoomRequestDto dto, @AuthenticationPrincipal MemberDetailsImpl userDetails){
//
//        log.info("member [ {} {} ] called createChatRoom", userDetails.getMember().getMemberId(),userDetails.getMember().getMemberNickname());
//        return ResponseEntity.ok(chatService.createChatRoom(userDetails.getMember(), dto));
//    }
//
//    @DeleteMapping("/chatroom/{id}")
//    public ResponseEntity<?> deleteChatRoom(@PathVariable String id){       // todo : 채팅방 삭제 - 사용자 인증, 인가 정보 필요 @AuthenticationPrincipal UserDetailsImpl userDetails,
//        // todo : 구현
//        log.info("member [ 수정필요(인증정보 부분) ] called deleteChatRoom");
//        return ResponseEntity.ok(null);
//    }
//
//    @PostMapping("/chatroom/{id}")
//    public ResponseEntity<?> addMemberToChatRoom(@PathVariable String id){       // todo : 채팅방 멤버 추가 - 사용자 인증, 인가 정보 필요 @AuthenticationPrincipal UserDetailsImpl userDetails,
//        // todo : 구현
//        log.info("member [ 수정필요(인증정보 부분) ] called deleteChatRoom");
//        return ResponseEntity.ok(null);
//    }
//
//}
