package com.example.canya.chat.entity;

import com.example.canya.chat.dto.ChatRoomRequestDto;
import com.example.canya.member.entity.Member;
import com.example.canya.timestamp.Timestamp;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Getter
@Entity
@NoArgsConstructor
public class ChatRoom extends Timestamp {

    @Id
    private String chatRoomId;
    private String chatRoomName;
    private String memberNickname;


    public ChatRoom(String chatRoomId, String chatRoomName, Member member) {
        this.chatRoomId = chatRoomId;
        this.chatRoomName = chatRoomName;
        this.memberNickname = member.getMemberNickname();
    }

    public ChatRoom(Member member,ChatRoomRequestDto requestDto, String uuid){
        this.chatRoomId = uuid;
        this.chatRoomName = requestDto.getChatRoomName();
        this.memberNickname = member.getMemberNickname();
    }
    public String chatDate(){
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 E요일 a hh:mm:ss", Locale.KOREA));
        return date;
    }

}
