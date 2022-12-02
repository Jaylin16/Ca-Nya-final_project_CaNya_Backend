package com.example.canya.community.service;

import com.example.canya.community.dto.CommunityRequestDto;
import com.example.canya.community.entity.Community;
import com.example.canya.community.repository.CommunityRepository;
import com.example.canya.member.entity.Member;
import com.example.canya.member.service.MemberDetailsImpl;
import com.example.canya.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public ResponseEntity<?> saveCommunity(CommunityRequestDto communityRequestDto, Member member, MultipartFile image) throws IOException {

        Community community = new Community(communityRequestDto, member, s3Uploader.upload(image, "communityImage"));

        communityRepository.save(community);

        return new ResponseEntity<>("작성이 완료되었습니다", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> updateCommunity(Long communityId, CommunityRequestDto communityRequestDto, MemberDetailsImpl memberDetails, MultipartFile image) throws IOException {

        Optional<Community> community = communityRepository.findById(communityId);

        if (community.isEmpty()){

            return new ResponseEntity<>("해당 게시글을 찾을 수 없습니다", HttpStatus.BAD_REQUEST);
        }

        if (!(community.get().getMember().getMemberId().equals(memberDetails.getMember().getMemberId()))) {

            return new ResponseEntity<>("작성자가 아닙니다", HttpStatus.BAD_REQUEST);
        }

        String originImage = community.get().getCommunityImage();

        String targetImage = "communityImage" + originImage.substring(originImage.lastIndexOf("/"));

        s3Uploader.deleteFile(targetImage);

        String newImage = s3Uploader.upload(image, "communityImage");

        community.get().update(communityRequestDto, newImage);

        return new ResponseEntity<>("수정이 완료되었습니다", HttpStatus.OK);
    }

}
