package com.example.canya.chatRoom.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Profile("stomp")
@EnableWebSocketMessageBroker
@Configuration
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    //SockJS 동시에 사용 가능하도록 설정 및 엔드 포인트 설정.
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp-chat")
                .setAllowedOrigins("http://localhost:8080")
                .withSockJS();
    }

    //메세지 브로커 설정.
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //client에서 send 요청을 처리한다.
        registry.setApplicationDestinationPrefixes("/publish");
        //해당 경로로 SimpleBroker를 등록한다. SimpleBroker는 해당하는 경로를 subscribe하는 client에게 메세지를 전달하는 간단한 작업을 수행한다.
        registry.enableSimpleBroker("/subscribe");
    }
}