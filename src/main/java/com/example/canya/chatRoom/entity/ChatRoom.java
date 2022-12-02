package com.example.canya.chatRoom.entity;

import com.example.canya.member.entity.Member;
import com.example.canya.timestamp.Timestamp;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor
public class ChatRoom extends Timestamp {

    @Id
    private String chatRoomId;
    private String chatRoomName;
    private String memberNickname;

    @Builder
    public ChatRoom(String chatRoomId, String chatRoomName, Member member) {
        this.chatRoomId = chatRoomId;
        this.chatRoomName = chatRoomName;
        this.memberNickname = member.getMemberNickname();
    }


}
