package com.example.canya.Board.Service;

import com.example.canya.Board.Dto.*;
import com.example.canya.Board.Entity.Board;
import com.example.canya.Board.Repository.BoardRepository;
import com.example.canya.Heart.Repository.HeartRepository;
import com.example.canya.Image.Entity.Image;
import com.example.canya.Image.Repository.ImageRepository;
import com.example.canya.Member.Entity.Member;
import com.example.canya.Member.Repository.MemberRepository;
import com.example.canya.Member.Service.MemberDetailsImpl;
import com.example.canya.Rating.Dto.RatingRequestDto;
import com.example.canya.Rating.Dto.RatingResponseDto;
import com.example.canya.Rating.Entity.Rating;
import com.example.canya.Rating.Repository.RatingRepository;
import com.example.canya.S3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ImageRepository imageRepository;
    private final RatingRepository ratingRepository;
    private final HeartRepository heartRepository;
    private final MemberRepository memberRepository;
    private final S3Uploader s3Uploader;

    public ResponseEntity<?> addBoards(List<Board> boardList, List<BoardResponseDto> returningDto){
        for (Board board : boardList) {
            Rating ratingList = ratingRepository.findRatingByBoardAndMemberId(board, board.getMember().getMemberId());

            List<Map.Entry<String, Double>> ratings = ratingList.getTwoHighestRatings(ratingList);

            RatingResponseDto ratingDto = new RatingResponseDto(ratings.get(0), ratings.get(1), ratingList);
            boolean isLiked = heartRepository.existsByBoardAndMember_MemberId(board, board.getMember().getMemberId());

            returningDto.add(new BoardResponseDto(board, ratingDto,isLiked));
        }
        return new ResponseEntity<>(returningDto,HttpStatus.OK);
    }
    public ResponseEntity<?> getBoards() {

        List<Board> allBoards = boardRepository.findTop8ByOrderByCreatedAtDesc();
        List<Board> bestBoards = boardRepository.findTop4ByOrderByTotalHeartCountDesc();
        List<Board> newBoards = boardRepository.findTop6ByOrderByCreatedAtDesc();

        List<Board> canyaCoffeeBoards =
                boardRepository.findTop3ByHighestRatingContainingOrderByTotalHeartCountDesc("coffee");
        List<Board> canyaMoodBoards =
                boardRepository.findTop3ByHighestRatingContainingOrderByTotalHeartCountDesc("mood");
        List<Board> canyaDessertBoards =
                boardRepository.findTop3ByHighestRatingContainingOrderByTotalHeartCountDesc("dessert");

        List<BoardResponseDto> bestDto = new ArrayList<>();
        List<BoardResponseDto> dessertResponseDto = new ArrayList<>();
        List<BoardResponseDto> coffeeResponseDto = new ArrayList<>();
        List<BoardResponseDto> moodResponseDto = new ArrayList<>();
        List<BoardResponseDto> newDto = new ArrayList<>();
        List<BoardResponseDto> allDto = new ArrayList<>();

        for (Board board : bestBoards) {
            BoardResponseDto boardResponseDto = new BoardResponseDto(board);

            bestDto.add(boardResponseDto);
        }

        addBoards(canyaCoffeeBoards,coffeeResponseDto);
        addBoards(canyaMoodBoards, moodResponseDto);
        addBoards(canyaDessertBoards, dessertResponseDto);
        addBoards(allBoards , allDto);
        addBoards(newBoards , newDto);
        return new ResponseEntity<>(new MainPageDto(coffeeResponseDto, moodResponseDto, dessertResponseDto, newDto, allDto, bestDto),HttpStatus.OK);
    }

    public ResponseEntity<?> getMainCategory(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        List<BoardResponseDto> keywordPick = new ArrayList<>();

        int boardNum = boardRepository.findBoardsByHighestRatingContainingOrderByTotalHeartCountDesc(keyword).size();

        Slice<Board> boardList = boardRepository.findBoardsByHighestRatingContainingOrderByTotalHeartCountDesc(keyword, pageable);

        for (Board board : boardList) {
            Rating ratingList = ratingRepository.findRatingByBoardAndMemberId(board, board.getMember().getMemberId());

            List<Map.Entry<String, Double>> ratings = ratingList.getTwoHighestRatings(ratingList);

            boolean isLiked = heartRepository.existsByBoardAndMember_MemberId(board, board.getMember().getMemberId());
            RatingResponseDto ratingDto = new RatingResponseDto(ratings.get(0), ratings.get(1), ratingList);

            keywordPick.add(new BoardResponseDto(board, ratingDto, isLiked));
        }

        return new ResponseEntity<>(new BoardResponseDto(keywordPick, size, boardNum, page), HttpStatus.OK);
    }
    // 이 부분은 query DSL 로 바꾸기.
    public ResponseEntity<?> searchBoard(String category, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<BoardResponseDto> boardResponseDtos = new ArrayList<>();
        int boardNum = 0;
        if (category.equals("memberNickname")) {

            List<Member> memberList = memberRepository.findMembersByMemberNicknameContaining(keyword);

            for (Member member : memberList) {

                boardNum += boardRepository.findBoardByMember(member).size();

                Slice<Board> boards = boardRepository.findBoardByMember(member, pageable);

                for (Board board : boards) {

                    boardResponseDtos.add(new BoardResponseDto(board));

                }
            }

            return new ResponseEntity<>(new BoardResponseDto(boardResponseDtos, size, boardNum, page), HttpStatus.OK);
        }
        if (category.equals("boardTitle")) {
            boardNum = boardRepository.findBoardsByBoardTitleContaining(keyword).size();

            Slice<Board> boardList = boardRepository.findBoardsByBoardTitleContaining(keyword, pageable);

            for (Board board : boardList) {

                boardResponseDtos.add(new BoardResponseDto(board));
            }
            return new ResponseEntity<>(new BoardResponseDto(boardResponseDtos, size, boardNum, page), HttpStatus.OK);
        }
        if (category.equals("boardContent")) {

            boardNum = boardRepository.findBoardsByBoardContentContaining(keyword).size();

            Slice<Board> boardLists = boardRepository.findBoardsByBoardContentContaining(keyword, pageable);

            for (Board board : boardLists) {

                boardResponseDtos.add(new BoardResponseDto(board));

            }
            return new ResponseEntity<>(new BoardResponseDto(boardResponseDtos, size, boardNum, page), HttpStatus.OK);
        }

        return new ResponseEntity<>("해당되는 카테고리를 넣어주세요", HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ResponseEntity<?> saveBoard(MemberDetailsImpl memberDetails) {
        Board board = boardRepository.save(new Board(memberDetails.getMember()));
        return new ResponseEntity<>(board.getBoardId(), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> editBoard(BoardRequestDto dto, List<MultipartFile> images, Long boardId, Member member, String[] urls) throws IOException {

        Board board = boardRepository.findById(boardId).orElse(null);
        List<Image> imageList = imageRepository.findAllByBoard(board);
        Rating rating = ratingRepository.findRatingByBoardAndMemberId(board, member.getMemberId());

        assert board != null;
        if (!Objects.equals(board.getMember().getMemberId(), member.getMemberId())) {
            return new ResponseEntity<>("작성자가 다릅니다.", HttpStatus.BAD_REQUEST);
        }

        if (member.getMemberId().equals(board.getMember().getMemberId())) {
            board.update(dto);

            RatingRequestDto ratingDto = new RatingRequestDto(dto.getRatings()[0], dto.getRatings()[1], dto.getRatings()[2], dto.getRatings()[3], dto.getRatings()[4], dto.getRatings()[5]);

            rating.update(ratingDto);

            List<Map.Entry<String, Double>> twoHighestRatings = rating.getTwoHighestRatings(rating);
            board.update(dto, twoHighestRatings.get(0).getKey(), twoHighestRatings.get(1).getKey());

            imageRepository.deleteAll(imageList);
            for (String url : urls) {
                imageRepository.save(new Image(board, url, board.getMember()));
            }
            if (images != null) {
                for (MultipartFile image : images) {
                    imageRepository.save(new Image(board, s3Uploader.upload(image, "boardImage"), board.getMember()));
                }
            }
        }
        return new ResponseEntity<>("수정이 완료되었습니다.", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> cancelBoard(Long boardId, Member member) {
        Optional<Board> board = boardRepository.findById(boardId);
        if (board.isEmpty()) {
            return new ResponseEntity<>("게시글이 존재 하지 않습니다", HttpStatus.BAD_REQUEST);
        }
        if (board.get().getMember() != member) {
            return new ResponseEntity<>("작성자가 일치하지 않습니다", HttpStatus.BAD_REQUEST);
        }
        boardRepository.deleteById(boardId);
        return new ResponseEntity<>("작성이 취소 되었습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> getBoardDetail(Long boardId) {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board == null) {
            return new ResponseEntity<>("존재 하지 않는 게시물입니다", HttpStatus.BAD_REQUEST);
        }
        boolean isLiked = heartRepository.existsByBoardAndMember_MemberId(board, board.getMember().getMemberId());
        List<Image> imageList = board.getImageList();
        Rating rating = ratingRepository.findRatingByBoard(board);

        BoardResponseDto responseDto = new BoardResponseDto(board, imageList, rating, isLiked);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
    // try to divide the sections up. takes too long to get the result at the moment.
    @Transactional
    public ResponseEntity<?> confirmBoard(BoardRequestDto dto, List<MultipartFile> images, Long boardId) throws IOException {
        Board board = boardRepository.findById(boardId).orElse(null);

        if (images != null) {
            for (MultipartFile image : images) {
                assert board != null;
                imageRepository.save(new Image(board, s3Uploader.upload(image, "boardImage"), board.getMember()));
            }
        }

        RatingRequestDto ratingDto = new RatingRequestDto(dto.getRatings()[0], dto.getRatings()[1], dto.getRatings()[2], dto.getRatings()[3], dto.getRatings()[4], dto.getRatings()[5]);
        assert board != null;
        Rating rating = new Rating(ratingDto, board, board.getMember());
        ratingRepository.save(rating);

        List<Map.Entry<String, Double>> twoHighestRatings = rating.getTwoHighestRatings(rating);
        board.update(dto, twoHighestRatings.get(0).getKey(), twoHighestRatings.get(1).getKey());
        boardRepository.save(board);

        return new ResponseEntity<>("게시글 작성이 완료 되었습니다.", HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<?> deleteBoard(Long boardId, Member member) {

        Optional<Board> board = boardRepository.findById(boardId);
        if (board.isEmpty()) {
            return new ResponseEntity<>("게시글이 존재 하지 않습니다", HttpStatus.BAD_REQUEST);
        }
        if (!Objects.equals(board.get().getMember().getMemberId(), member.getMemberId())) {
            return new ResponseEntity<>("작성자가 일치 하지 않습니다", HttpStatus.BAD_REQUEST);
        }
        boardRepository.deleteById(boardId);
        return new ResponseEntity<>("삭제가 완료 되었습니다", HttpStatus.OK);
    }
}