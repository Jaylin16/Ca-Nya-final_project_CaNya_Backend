package com.example.canya.chatRoom.controller;

import com.example.canya.chatRoom.service.ChatService;
import com.example.canya.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/chat")
public class ChatRoomController {

//    private final ChatRoomRepository repository;
    private final ChatService chatService;
//    private final String listViewName;
//    private final String detailViewName;
//    private final AtomicInteger seq = new AtomicInteger(0);

//    @Autowired
//    public ChatRoomController(ChatRoomRepository repository, @Value("${viewname.chatroom.list}") String listViewName, @Value("${viewname.chatroom.detail}") String detailViewName) {
//        this.repository = repository;
//        this.listViewName = listViewName;
//        this.detailViewName = detailViewName;
//    }

    @Operation(summary = "채팅방 생성", description = "채팅방 생성 기능")
    @PostMapping("/create")
    public ResponseEntity<?> createChatRoom(@RequestBody String chatRoomName, @AuthenticationPrincipal Member member) {
        return chatService.createChatRoom(chatRoomName, member);
    }

    //현재 생성되어있는 채팅방 리스트 보여줌.
    @Operation(summary = "채팅방 전체 리스트", description = "채팅방 전체 리스트 불러오기 기능")
    @GetMapping("/rooms")
    public ResponseEntity<?> rooms() {
        return chatService.allRooms();
    }
//    @GetMapping("/rooms")
//    public String rooms(Model model) {
//        model.addAttribute("rooms", repository.getChatRooms());
//        return listViewName;
//    }

    //생성된 방의 정보를 보여줌.
    @Operation(summary = "채팅방 세부 내역", description = "채팅방 세부 내역 불러오기 기능")
    @GetMapping("/rooms/{chatRoomId}")
    public ResponseEntity<?> roomDetails(@PathVariable String chatRoomId) {
        return chatService.roomDetails(chatRoomId);
    }

//    @GetMapping("/rooms/{chatRoomId}")
//    public String room(@PathVariable String id, Model model) {
//        ChatRoom room = repository.getChatRoom(id);
//        model.addAttribute("room", room);
//        model.addAttribute("member", "member" + seq.incrementAndGet());
//
//        return detailViewName;
//    }



}
