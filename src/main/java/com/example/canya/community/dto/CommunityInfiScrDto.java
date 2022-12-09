package com.example.canya.community.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class CommunityInfiScrDto {

    private List<CommunityResponseDto> communityList;
    private Boolean isLast;

    public CommunityInfiScrDto(List<CommunityResponseDto> community, Boolean isLast) {
        this.communityList = community;
        this.isLast = isLast;
    }
}
