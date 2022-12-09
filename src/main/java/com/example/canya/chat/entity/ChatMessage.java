package com.example.canya.chat.entity;

import com.example.canya.chat.dto.ChatMessageRequestDto;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @Column(nullable = false)
    private String sender;

    @Column
    private String message;

    @Column
    private String date;

    private String roomId;


   public ChatMessage(ChatRoom chatRoom , ChatMessageRequestDto dto){
       this.roomId = chatRoom.getChatRoomId();
       this.message = dto.getMessage();
       this.sender = dto.getSender();
   }
}
