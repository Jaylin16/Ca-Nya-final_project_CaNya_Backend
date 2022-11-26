package com.example.canya.Category.Service;

import com.example.canya.Board.Dto.BoardResponseDto;
import com.example.canya.Board.Entity.Board;
import com.example.canya.Board.Repository.BoardRepository;
import com.example.canya.Board.Service.BoardService;
import com.example.canya.Heart.Repository.HeartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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


    public void addSearchBoard(Slice<Board> boardList, List<BoardResponseDto> boardResponseDtos) {

        for (Board board : boardList) {
            boolean isLiked = heartRepository.existsByBoardAndMember_MemberId(board, board.getMember().getMemberId());
            boardResponseDtos.add(new BoardResponseDto(board, isLiked));
        }
    }

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
