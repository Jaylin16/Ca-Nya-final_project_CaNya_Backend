package com.example.canya.category.controller;

import com.example.canya.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "메인 카테고리별 조회", description = "메인 페이지 카테고리별 조회 기능")
    @GetMapping("/board/main/{keyword}")
    public ResponseEntity<?> getMainCategory(@PathVariable String keyword,
                                             @RequestParam(value = "page",required = false) Integer page,
                                             @RequestParam(value = "size" , required = false) Integer size){
        Integer pageTemp = page -1;

        return categoryService.getMainCategory(keyword,pageTemp, size);
    }

    @GetMapping("/board/search/{category}/{keyword}")
    public ResponseEntity<?> searchBoard(@PathVariable String category,
                                         @PathVariable String keyword,
                                         @RequestParam(required = false) Integer page,
                                         @RequestParam(required = false) Integer size){

        Integer pageTemp = page -1;
        return categoryService.searchBoard(category,keyword,pageTemp,size);
    }
}
