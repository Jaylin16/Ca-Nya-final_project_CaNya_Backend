package com.example.canya.chat.controller;

import com.example.canya.chat.dto.ChatMessageRequestDto;
import com.example.canya.chat.dto.ChatMessageResponseDto;
import com.example.canya.chat.entity.ChatMessage;
import com.example.canya.chat.entity.ChatRoom;
import com.example.canya.chat.entity.MessageType;
import com.example.canya.chat.repository.ChatMessageRepository;
import com.example.canya.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    @MessageMapping("/hello/{id}")
    public void socketHandle(@DestinationVariable("id") String id, ChatMessageRequestDto message) throws Exception {

        log.info("socketHandle: id={}, MessageDto={}", id, message);
        System.out.println("message = " + message.getMessage());
        System.out.println("message type = " + message.getType());
        System.out.println("message sender = " + message.getSender());


        if (message.getType().equals(MessageType.MESSAGE.getMethod())) {
            System.out.println("method matches");
            ChatRoom chatRoom = chatRoomRepository.findById(message.getRoomId()).orElse(null);
            if (chatRoom == null) {
                simpMessagingTemplate.convertAndSend("/topic/greetings/" + id,
                        new ChatMessageResponseDto(MessageType.ERROR.getMethod(), null, null, "존재하지 않는 채팅방 입니다.", null));
                return;
            }
            ChatMessage save = chatMessageRepository.save(new ChatMessage(chatRoom, message));

            log.trace("socketHandle MESSAGE send to : (id){}", chatRoom.getMemberNickname());
            simpMessagingTemplate.convertAndSend("/topic/greetings/" + chatRoom.getMemberNickname(),
                        new ChatMessageResponseDto(message.getType(), message.getRoomId(), message.getSender(), message.getMessage(), save.getDate()));

//            String[] s = chatRoom.getUserIdList().split(" ");
//            for (String s1 : s) {
//                log.trace("socketHandle MESSAGE send to : (id){}", s1);
//                simpMessagingTemplate.convertAndSend("/topic/greetings/" + s1,
//                        new SendMessageDto(message.getAction(), message.getChatRoomId(), message.getUserName(), message.getContent(), save.getCreatedAt()));
//            }

        } else {
            simpMessagingTemplate.convertAndSend("/topic/greetings/" + id,
                    new ChatMessageResponseDto(MessageType.ERROR.getMethod(), "null", "null", "알 수 없는 요청 입니다.", "null"));
        }
    }
}
