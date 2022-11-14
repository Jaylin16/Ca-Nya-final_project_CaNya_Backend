package com.example.canya.Image.Controller;

import com.example.canya.Image.Service.ImageService;
import com.example.canya.Member.Service.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @DeleteMapping("/auth/boards/image-delete/{imageId}")
    public ResponseEntity<?> deleteImage(@AuthenticationPrincipal MemberDetailsImpl memberDetails, @PathVariable Long imageId){
        return imageService.deleteImage(memberDetails.getMember(),imageId);
    }
}
