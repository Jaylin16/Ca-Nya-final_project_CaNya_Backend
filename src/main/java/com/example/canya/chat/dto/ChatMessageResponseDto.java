package com.example.canya.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ChatMessageResponseDto {
    private String roomId;
    private String type;
    private String sender;
    private String message;
    private String date;

    @Builder
    public ChatMessageResponseDto(String roomId, String type, String sender, String message, String date){
        this.roomId = roomId;
        this.type = type;
        this.sender = sender;
        this.message = message;
        this.date = date;
    }
}
