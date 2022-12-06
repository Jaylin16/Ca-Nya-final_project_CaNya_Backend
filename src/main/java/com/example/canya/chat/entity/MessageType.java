package com.example.canya.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageType {
    MESSAGE("MESSAGE"),
    ERROR("ERROR");

    private final String method;
}
