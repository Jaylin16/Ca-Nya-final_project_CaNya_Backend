package com.example.canya.community.service;

import com.example.canya.community.dto.CommunityResponseDto;
import com.example.canya.community.entity.Community;
import com.example.canya.community.repository.CommunityRepository;
import com.example.canya.member.entity.Member;
import com.example.canya.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public ResponseEntity<?> getCommunityList() {
        List<Community> communities = communityRepository.findAll();

        List<CommunityResponseDto> communityList = new ArrayList<>();
        for(Community community : communities) {
            CommunityResponseDto communityResponseDto = new CommunityResponseDto(community);

            communityList.add(communityResponseDto);
        }

        return new ResponseEntity<>(communityList, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> getCommunityDetail(Long communityId) {
        Optional<Community> community = communityRepository.findById(communityId);
        if(community.isEmpty()) {
            return new ResponseEntity<>("해당 커뮤니티 글을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        CommunityResponseDto communityResponseDto = new CommunityResponseDto(community.get());

        return new ResponseEntity<>(communityResponseDto, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deleteCommunity(Long communityId, Member member) {
        Optional<Community> community = communityRepository.findById(communityId);
        if(community.isEmpty()) {
            return new ResponseEntity<>("삭제하려는 글을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        if(!(Objects.equals(community.get().getMember().getMemberId(), member.getMemberId()))) {
            return new ResponseEntity<>("본인이 작성한 글만 삭제 가능합니다.", HttpStatus.BAD_REQUEST);
        }

        String communityImage = community.get().getCommunityImage();
        String targetCommunityImage = "communityImage" + communityImage.substring(communityImage.lastIndexOf("/"));

        s3Uploader.deleteFile(targetCommunityImage);

        communityRepository.deleteById(communityId);

        return new ResponseEntity<>("삭제가 완료되었습니다.", HttpStatus.OK);
    }
}
