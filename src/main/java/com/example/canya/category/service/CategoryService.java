package com.example.canya.category.service;

import com.example.canya.annotations.SetPageable;
import com.example.canya.board.dto.BoardResponseDto;
import com.example.canya.board.entity.Board;
import com.example.canya.board.repository.BoardRepository;
import com.example.canya.board.service.BoardService;
import com.example.canya.heart.repository.HeartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final BoardRepository boardRepository;
    private final HeartRepository heartRepository;
    private final BoardService boardService;


    public ResponseEntity<?> getMainCategory(String keyword, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        List<BoardResponseDto> keywordPick = new ArrayList<>();
        int boardNum = boardRepository.findBoardsByHighestRatingContainingOrderByTotalHeartCountDesc(keyword).size();
        List<Board> boardList = boardRepository.findBoardsByHighestRatingContainingOrderByTotalHeartCountDesc(keyword, pageable);

        boardService.addBoards(boardList, keywordPick);

        return new ResponseEntity<>(new BoardResponseDto(keywordPick, size, boardNum, page), HttpStatus.OK);
    }
    public ResponseEntity<?> getMainCategories(String keyword, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        List<BoardResponseDto> keywordPick = new ArrayList<>();
        int boardNum = 0;

        if(Objects.equals(keyword, "인기")){

            System.out.println("인기 조회");
            boardNum = boardRepository.findAll().size();
            System.out.println("boardNum in 인기= " + boardNum);;
            List<Board> boardList = boardRepository.findBoardsByOrderByTotalHeartCountDesc(pageable);
            boardService.addBoards(boardList, keywordPick);

        }
        if(Objects.equals(keyword,"최신")){

            System.out.println("최신 조회");
            boardNum = boardRepository.findAll().size();
            System.out.println("boardNum in 최신= " + boardNum);
            List<Board> boardList = boardRepository.findBoardsByOrderByCreatedAtDesc(pageable);
            System.out.println(boardList.get(0));
            boardService.addBoards(boardList, keywordPick);

        }
        if(Objects.equals(keyword,"전체")){

            System.out.println("전체 조회");
            boardNum = boardRepository.findAll().size();
            System.out.println("boardNum in 전체= " + boardNum);
            List<Board> boardList = boardRepository.findBoardsByOrderByCreatedAtDesc(pageable);
            Collections.shuffle(boardList);
            System.out.println(boardList.get(0));
            boardService.addBoards(boardList, keywordPick);

        }
        return new ResponseEntity<>(new BoardResponseDto(keywordPick, size, boardNum, page), HttpStatus.OK);

    }


    public void addSearchBoard(Slice<Board> boardList, List<BoardResponseDto> boardResponseDtos) {

        for (Board board : boardList) {
            boolean isLiked = heartRepository.existsByBoardAndMember_MemberId(board, board.getMember().getMemberId());
            boardResponseDtos.add(new BoardResponseDto(board, isLiked));
        }
    }

    @SetPageable
    public ResponseEntity<?> searchBoard(String category, String keyword, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        List<BoardResponseDto> boardResponseDtos = new ArrayList<>();

        if (Objects.equals(keyword, "")) {
            List<Board> boards = boardRepository.findAll();
            List<BoardResponseDto> dtoList = new ArrayList<>();

            for (Board board : boards) {
                boolean isLiked = heartRepository.existsByBoardAndMember_MemberId(board, board.getMember().getMemberId());
                dtoList.add(new BoardResponseDto(board, isLiked));
            }

            return new ResponseEntity<>(new BoardResponseDto(boardResponseDtos, size, boards.size(), page), HttpStatus.OK);
        }

        if (category.equals("memberNickname")) {
            int boardNum = boardRepository.findBoardByMemberMemberNicknameContaining(keyword).size();
            Slice<Board> boardList = boardRepository.findBoardByMemberMemberNicknameContaining(keyword, pageable);

            return getSearchResult(boardList, boardResponseDtos, size, boardNum, page);
        }

        if (category.equals("boardTitle")) {
            int boardNum = boardRepository.findBoardsByBoardTitleContaining(keyword).size();
            Slice<Board> boardList = boardRepository.findBoardsByBoardTitleContaining(keyword, pageable);

            return getSearchResult(boardList, boardResponseDtos, size, boardNum, page);
        }

        if (category.equals("boardContent")) {
            int boardNum = boardRepository.findBoardsByBoardContentContaining(keyword).size();
            Slice<Board> boardList = boardRepository.findBoardsByBoardContentContaining(keyword, pageable);

            return getSearchResult(boardList, boardResponseDtos, size, boardNum, page);
        }

        return new ResponseEntity<>("해당되는 카테고리를 넣어주세요", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> getSearchResult(Slice<Board> boardList, List<BoardResponseDto> boardResponseDtos,
                                             int size, int boardNum, int page) {

        if (boardList.isEmpty()) {
            return new ResponseEntity<>("해당 조회 결과가 없습니다.", HttpStatus.OK);
        } else {
            addSearchBoard(boardList, boardResponseDtos);
        }

        return new ResponseEntity<>(new BoardResponseDto(boardResponseDtos, size, boardNum, page), HttpStatus.OK);
    }


}
