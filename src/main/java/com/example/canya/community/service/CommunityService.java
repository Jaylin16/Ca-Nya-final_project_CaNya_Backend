package com.example.canya.community.service;

import com.example.canya.community.dto.CommunityInfiScrDto;
import com.example.canya.community.dto.CommunityResponseDto;
import com.example.canya.community.entity.Community;
import com.example.canya.community.repository.CommunityRepository;
import com.example.canya.member.entity.Member;
import com.example.canya.community.dto.CommunityRequestDto;
import com.example.canya.member.service.MemberDetailsImpl;
import com.example.canya.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public ResponseEntity<?> saveCommunity(CommunityRequestDto communityRequestDto, Member member, MultipartFile image) throws IOException {

        Community community = new Community(communityRequestDto, member, image !=null ?s3Uploader.upload(image, "communityImage"):"https://img.siksinhot.com/article/1557391416433073.jpg");
        communityRepository.save(community);

        return new ResponseEntity<>("작성이 완료되었습니다", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> updateCommunity(Long communityId, CommunityRequestDto communityRequestDto, MemberDetailsImpl memberDetails, MultipartFile image, String url) throws IOException {

        Optional<Community> community = communityRepository.findById(communityId);

        if (community.isEmpty()){
            return new ResponseEntity<>("해당 게시글을 찾을 수 없습니다", HttpStatus.BAD_REQUEST);
        }

        if (!(community.get().getMember().getMemberId().equals(memberDetails.getMember().getMemberId()))) {
            return new ResponseEntity<>("작성자가 아닙니다", HttpStatus.BAD_REQUEST);
        }

        if (url != null) {
            community.get().update(communityRequestDto, url);
        }

        if (image != null) {
            String originImage = community.get().getCommunityImage();
            String targetImage = "communityImage" + originImage.substring(originImage.lastIndexOf("/"));

            s3Uploader.deleteFile(targetImage);

            String newImage = s3Uploader.upload(image, "communityImage");
            community.get().update(communityRequestDto, newImage);
        }else {
            String defaultImage = "https://img.siksinhot.com/article/1557391416433073.jpg";
            community.get().update(communityRequestDto, defaultImage);
        }

        return new ResponseEntity<>("수정이 완료되었습니다", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> getCommunityList(Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        Slice<Community> communities = communityRepository.findAll(pageable);

        List<CommunityResponseDto> communityList = new ArrayList<>();
        for (Community community : communities) {
            CommunityResponseDto communityResponseDto = new CommunityResponseDto(community);

            communityList.add(communityResponseDto);
        }

        CommunityInfiScrDto communityInfiScrDto = new CommunityInfiScrDto(communityList, communities.isLast());

        return new ResponseEntity<>(communityInfiScrDto, HttpStatus.OK);

    }

    @Transactional
    public ResponseEntity<?> getCommunityDetail(Long communityId) {
        Optional<Community> community = communityRepository.findById(communityId);
        if(community.isEmpty()) {
            return new ResponseEntity<>("해당 커뮤니티 글을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        CommunityResponseDto communityResponseDto = new CommunityResponseDto(community.get());
        community.get().addHitCount();

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
