package com.example.canya.chatRoom.service;

import com.example.canya.chatRoom.entity.ChatMessage;
import com.example.canya.chatRoom.entity.ChatRoom;
import com.example.canya.chatRoom.entity.MessageType;
import com.example.canya.chatRoom.repository.ChatRoomRepository;
import com.example.canya.chatRoom.stomp.MessageSendUtils;
import com.example.canya.member.entity.Member;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ObjectMapper objectMapper;
    private final ChatRoomRepository chatRoomRepository;
    private Map<String, ChatRoom> chatRooms;
    private final Map<String, ChatRoom> chatRoomMap;
    private Set<WebSocketSession> sessions = new HashSet<>();


    //채팅방 생성.
    public ResponseEntity<?> createChatRoom (@NonNull String chatRoomName, Member member) {

        String chatRoomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .chatRoomId(chatRoomId)
                .chatRoomName(chatRoomName)
                .member(member)
                .build();
        chatRoomRepository.save(chatRoom);

        ChatRoom created = new ChatRoom(chatRoomId, chatRoomName, member);

        return new ResponseEntity<> (created,HttpStatus.OK);
    }

    //채팅방 시작시 session 추가.
    private void join(WebSocketSession session) {
        sessions.add(session);
    }

    //입장시 default값 메세지 날리는 부분.
    public void handleMessage(WebSocketSession session, ChatMessage chatMessage, ObjectMapper objectMapper) {

        if (chatMessage.getType() == MessageType.JOIN) {
            join(session);
            chatMessage.setMessage(chatMessage.getWriter() + "님이 입장했습니다.");
        }

        send(chatMessage, objectMapper);
    }

    //message 보내는 부분.
    private <T> void send(T messageObject, ObjectMapper objectMapper) {
        try {
            TextMessage message = new TextMessage(objectMapper.writeValueAsString(messageObject));
            sessions.parallelStream().forEach(session -> MessageSendUtils.sendMessage(session, message));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    //채팅방 삭제.
    public void remove(WebSocketSession target) {
        String targetId = target.getId();
        sessions.removeIf(session -> session.getId().equals(targetId));
    }

//    @Getter
//    private final Collection<ChatRoom> chatRooms;

//    public ChatRoomRepository() {
//        chatRoomMap = Collections.unmodifiableMap(
//                Stream.of(ChatRoom.create("1번방"), ChatRoom.create("2번방"), ChatRoom.create("3번방"))
//                        .collect(Collectors.toMap(ChatRoom::getChatRoomId, Function.identity())));
//
//        chatRooms = Collections.unmodifiableCollection(chatRoomMap.values());
//    }

//    public ChatRoom getChatRoom(String id) {
//        return chatRoomMap.get(id);
//    }

    //현재 생성되어있는 채팅방 리스트 보여줌. (Dto 작업 해주기)
    public ResponseEntity<?> allRooms() {
        List<ChatRoom> chatRoomList= chatRoomRepository.findAll();

        return new ResponseEntity<> (chatRoomList, HttpStatus.OK);
    }

//    //채팅방 삭제
//    public void remove(WebSocketSession session) {
//        this.chatRooms.parallelStream().forEach(chatRoom -> chatRoom.remove(session));
//    }

    //Dto 작업 해주기.
    public ResponseEntity<?> roomDetails(String chatRoomId) {
        Optional<ChatRoom> chatRoom = chatRoomRepository.findById(chatRoomId);

        if(chatRoom.isEmpty()) {
            return new ResponseEntity<>("생성된 방이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        String roomName = chatRoom.get().getChatRoomName();

        return new ResponseEntity<> (roomName, HttpStatus.OK);
    }
}
