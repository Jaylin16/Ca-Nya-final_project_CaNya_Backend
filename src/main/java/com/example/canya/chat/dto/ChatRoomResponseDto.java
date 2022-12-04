package com.example.canya.chat.dto;

import com.example.canya.chat.entity.ChatRoom;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomResponseDto {

    private String chatRoomId;
    private String chatRoomName;
    private String createdBy;

    public ChatRoomResponseDto(ChatRoom chatRoom) {
        this.chatRoomId = chatRoom.getChatRoomId();
        this.chatRoomName = chatRoom.getChatRoomName();
        this.createdBy = chatRoom.getMemberNickname();
    }
}
