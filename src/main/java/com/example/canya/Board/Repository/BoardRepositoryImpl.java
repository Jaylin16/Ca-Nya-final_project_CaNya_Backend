package com.example.canya.Board.Repository;

import com.example.canya.Board.Dto.BoardResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BoardResponseDto> searchByFullTextBooleanTest(String word, String mode, int page, int size, String field) {
        return null;
    }

    @Override
    public int searchByFullTextBooleanCount(String word, String mode, String field) {
        return 0;
    }

    @Override
    public int searchByIsbnCountQuery(String word, String mode, String field) {
        return 0;
    }
}
