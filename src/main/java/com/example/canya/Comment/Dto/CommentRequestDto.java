package com.example.canya.Comment.Dto;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {

    @NotNull
    private String commentContent;

}
