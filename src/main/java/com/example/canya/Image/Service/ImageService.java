package com.example.canya.Image.Service;

import com.example.canya.Image.Entity.Image;
import com.example.canya.Image.Repository.ImageRepository;
import com.example.canya.Member.Entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    @Transactional
    public ResponseEntity<?> deleteImage(Member member, Long imageId) {

        Image image = imageRepository.findById(imageId).orElse(null);
        if(!Objects.equals(image.getMember().getMemberId(), member.getMemberId())){
            return new ResponseEntity<>("작성자가 다릅니다", HttpStatus.BAD_REQUEST);
        }
        imageRepository.deleteById(imageId);

        return new ResponseEntity<>("사진 삭제가 완료되었습니다.",HttpStatus.OK);
    }
}
