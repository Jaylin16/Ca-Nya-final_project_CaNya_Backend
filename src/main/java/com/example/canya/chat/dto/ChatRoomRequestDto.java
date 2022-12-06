package com.example.canya.chat.dto;

import com.example.canya.chat.entity.ChatRoom;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomRequestDto {
    private String chatRoomId;
    private String memberNickname;
    private String chatRoomName;


    public ChatRoomRequestDto(ChatRoom entity) {
        this.chatRoomId = entity.getChatRoomId();
        this.memberNickname = entity.getMemberNickname();
        this.chatRoomName = entity.getChatRoomName();
    }
}
