package com.example.canya.Image.Controller;

import com.example.canya.Image.Service.ImageService;
import com.example.canya.Member.Service.MemberDetailsImpl;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "Image (게시글 이미지)")
public class ImageController {
    private final ImageService imageService;

    @Operation(summary = "게시글 이미지 삭제", description = "게시글 이미지 삭제 기능")
    @DeleteMapping("/auth/boards/image-delete/{imageId}")
    public ResponseEntity<?> deleteImage(@AuthenticationPrincipal MemberDetailsImpl memberDetails, @PathVariable Long imageId){
        return imageService.deleteImage(memberDetails.getMember(),imageId);
    }
}
