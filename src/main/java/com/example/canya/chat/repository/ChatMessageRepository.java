package com.example.canya.chat.repository;

import com.example.canya.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
//    List<ChatMessage> findAllByChatRoomId(String chatRoomId);

}
