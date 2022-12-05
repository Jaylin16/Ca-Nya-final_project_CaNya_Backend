package com.example.canya.communityComment.dto;

import com.sun.istack.NotNull;
import lombok.Getter;

@Getter
public class CommunityCommentRequestDto {

    @NotNull
    private String communityCommentContent;
}
