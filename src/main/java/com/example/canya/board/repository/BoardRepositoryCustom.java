package com.example.canya.board.repository;

import com.example.canya.board.dto.BoardResponseDto;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepositoryCustom {
    List<BoardResponseDto> searchByFullTextBooleanTest(@Param("word") String word, String mode, int page, int size, String field);
    int searchByFullTextBooleanCount(@Param("word") String word,String mode, String field);
    int searchByIsbnCountQuery(@Param("word") String word,String mode, String field);
}