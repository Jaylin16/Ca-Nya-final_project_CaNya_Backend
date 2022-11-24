package com.example.canya.Board.Repository;

import com.example.canya.Board.Dto.BoardResponseDto;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepositoryCustom {
    List<BoardResponseDto> searchByFullTextBooleanTest(@Param("word") String word, String mode, int page, int size, String field);
    int searchByFullTextBooleanCount(@Param("word") String word,String mode, String field);
    int searchByIsbnCountQuery(@Param("word") String word,String mode, String field);
}
