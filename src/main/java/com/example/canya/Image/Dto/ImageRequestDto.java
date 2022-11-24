package com.example.canya.Image.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
@Getter
@NoArgsConstructor
public class ImageRequestDto {
    private MultipartFile imageFile;
}
