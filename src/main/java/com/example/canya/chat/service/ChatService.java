//package com.example.canya.chat.service;
//
//import com.example.canya.chat.dto.ChatMessageResponseDto;
//import com.example.canya.chat.dto.ChatRoomRequestDto;
//import com.example.canya.chat.dto.ChatRoomResponseDto;
//import com.example.canya.chat.entity.ChatMessage;
//import com.example.canya.chat.entity.ChatRoom;
//import com.example.canya.chat.repository.ChatMessageRepository;
//import com.example.canya.chat.repository.ChatRoomRepository;
//import com.example.canya.member.entity.Member;
//import com.example.canya.member.repository.MemberRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.PathVariable;
//
//import java.util.*;
//
//@RequiredArgsConstructor
//@Slf4j
//@Service
//public class ChatService {
//
//    private final MemberRepository memberRepository;
//    private final ChatRoomRepository chatRoomRepository;
//    public List getChatRoom(Long id){       // 채팅방 목록 조회 ( 재접속시 )
//        Member member = memberRepository.findById(id).orElseThrow();
//        List list = new ArrayList<ChatRoom>();
//
//        String chatRoomIdList = member.getChatRoomIdList();
//        if(chatRoomIdList== null){
//            return null;
//        }
//        String[] s = chatRoomIdList.split(" ");
//
//        for (String s1 : s) {   // todo : 채팅방 이름이 대화 상대일 경우 로직
//            log.debug("유저 [{}] 채팅방 목록조회 중 ID={}", member.getName(), s1);
//            list.add(new ChatRoomDto(chatRoomRepository.findById(Long.parseLong(s1)).orElseThrow(() -> new GlobalException(ErrorCode.NO_SUCH_CHATROOM))));
//        }
//        return list;
//    }
//
//    public Long createChatRoom(Member member, ChatRoomRequestDto requestDto){   // todo : 유효성 검증로직 추가 (멤버리스트) , dto userNameList 수정
//        member = memberRepository.findById(member.getMemberId()).orElseThrow(()-> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));
//
//        requestDto.addUser(member);
//        ChatRoom save = chatRoomRepository.save(new ChatRoom(requestDto));
//
//        String[] s = save.getUserIdList().split(" ");
//        for (String s1 : s) {
//            member = memberRepository.findById(Long.parseLong(s1)).orElseThrow(()-> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));
//            String chatRoomIdList = member.getChatRoomIdList();
//            if(chatRoomIdList==null){
//                member.setChatRoomIdList(save.getChatRoomId().toString());
//            }else{
//                member.setChatRoomIdList(chatRoomIdList + " " + save.getChatRoomId().toString());
//            }
//        }
//
//        return save.getChatRoomId();
//    }
//
//
//    public ResponseDto getChatRoomHistory(@PathVariable String id, Member member){
//
//        if(!member.getChatRoomIdList().contains(id)){
//            throw new GlobalException(ErrorCode.NO_SUCH_CHATROOM);
//        }
//
//        List<ChatRecord> records = chatRoomRepository.findById(Long.parseLong(id))
//                .orElseThrow(() -> new GlobalException(ErrorCode.NO_SUCH_CHATROOM))
//                .getRecords();
//        List<ChatRecordResponseDto> recordList = new ArrayList<>();
//        for (ChatRecord record : records) {
//            recordList.add(new ChatRecordResponseDto(record));
//        }
//        return ResponseDto.success( recordList);
//    }
//}
