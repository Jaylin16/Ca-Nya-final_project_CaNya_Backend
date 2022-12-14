package com.example.canya.board.service;

import com.example.canya.annotations.AddImage;
import com.example.canya.annotations.VerifyMemberBoard;
import com.example.canya.board.dto.*;
import com.example.canya.board.entity.Board;
import com.example.canya.board.repository.BoardRepository;
import com.example.canya.heart.repository.HeartRepository;
import com.example.canya.image.entity.Image;
import com.example.canya.image.repository.ImageRepository;
import com.example.canya.member.entity.Member;
import com.example.canya.member.service.MemberDetailsImpl;
import com.example.canya.rating.dto.RatingRequestDto;
import com.example.canya.rating.dto.RatingResponseDto;
import com.example.canya.rating.entity.Rating;
import com.example.canya.timestamp.repository.RatingRepository;
import com.example.canya.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ImageRepository imageRepository;
    private final RatingRepository ratingRepository;
    private final HeartRepository heartRepository;
    private final S3Uploader s3Uploader;

    public ResponseEntity<?> getBoards() {

        List<Board> allBoards = boardRepository.findTop8ByIsReadyTrueOrderByCreatedAtDesc();
        List<Board> bestBoards = boardRepository.findTop4ByIsReadyTrueOrderByTotalHeartCountDesc();
        List<Board> newBoards = boardRepository.findTop6ByIsReadyTrueOrderByCreatedAtDesc();

        List<Board> canyaCoffeeBoards =
                boardRepository.findTop3ByHighestRatingContainingOrderByTotalHeartCountDesc("커피");
        List<Board> canyaMoodBoards =
                boardRepository.findTop3ByHighestRatingContainingOrderByTotalHeartCountDesc("분위기");
        List<Board> canyaDessertBoards =
                boardRepository.findTop3ByHighestRatingContainingOrderByTotalHeartCountDesc("디저트");

        List<BoardResponseDto> bestDto = new ArrayList<>();
        List<BoardResponseDto> dessertResponseDto = new ArrayList<>();
        List<BoardResponseDto> coffeeResponseDto = new ArrayList<>();
        List<BoardResponseDto> moodResponseDto = new ArrayList<>();
        List<BoardResponseDto> newDto = new ArrayList<>();
        List<BoardResponseDto> allDto = new ArrayList<>();


        addBoards(canyaCoffeeBoards, coffeeResponseDto);
        addBoards(canyaMoodBoards, moodResponseDto);
        addBoards(canyaDessertBoards, dessertResponseDto);

        addBoards(bestBoards, bestDto);
        addBoards(newBoards, newDto);
        addBoards(allBoards, allDto);


        return new ResponseEntity<>(new MainPageDto(coffeeResponseDto, moodResponseDto, dessertResponseDto,
                newDto, allDto, bestDto), HttpStatus.OK);

    }

    public void addBoards(List<Board> boardList, List<BoardResponseDto> returningDto) {
        System.out.println(boardList);
        for (Board board : boardList) {
            if (board.getBoardContent() == null || board.getBoardTitle() == null) {
                boardList.remove(board);
                continue;
            }

            Rating ratingList = ratingRepository.findRatingByBoardAndMemberId(board, board.getMember().getMemberId());
            List<String> ratings = ratingList.getTwoHighestRatings(ratingList);
            RatingResponseDto ratingDto = new RatingResponseDto(ratingList, ratings);

            boolean isLiked = heartRepository.existsByBoardAndMember_MemberId(board, board.getMember().getMemberId());

            returningDto.add(new BoardResponseDto(board, ratingDto, isLiked));

        }
        new ResponseEntity<>(returningDto, HttpStatus.OK);
    }

    @Transactional
    @VerifyMemberBoard
    public ResponseEntity<?> saveBoard(Member member) {

        int lastBoardIndex = boardRepository.findBoardByMember(member).size();

        if (lastBoardIndex == 0) {
            Board board = boardRepository.save(new Board(member));

            return new ResponseEntity<>(board.getBoardId(), HttpStatus.OK);
        }

        Board board = boardRepository.save(new Board(member));

        return new ResponseEntity<>(board.getBoardId(), HttpStatus.OK);

    }

    public ResponseEntity<?> getBoardDetail(Long boardId, MemberDetailsImpl memberDetails) {
        Board board = boardRepository.findById(boardId).orElseThrow();
        boolean isLiked = heartRepository.existsByBoardAndMember_MemberId(board, memberDetails.getMember().getMemberId());
        BoardResponseDto responseDto = new BoardResponseDto(board, isLiked);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Transactional
    @VerifyMemberBoard
    @AddImage
    public ResponseEntity<?> editBoard(BoardRequestDto dto, Member member, String[] urls, List<MultipartFile> images, Long boardId) throws IOException {

        Board board = boardRepository.findById(boardId).get();
        List<Image> imageList = imageRepository.findAllByBoard(board);
        List<String> imageUrlList = new ArrayList<>();

        for (Image image : imageList) {
            imageUrlList.add(image.getImageUrl());

        }

        for (String url : urls) {
            for (int j = 0; j < imageUrlList.size(); j++) {
                if (Objects.equals(url, imageUrlList.get(j))) {
                    imageUrlList.remove(j);
                }
            }
        }

        if (images != null) {
            for (MultipartFile image : images) {
                imageRepository.save(new Image(board, s3Uploader.upload(image, "boardImage"), member));
            }
            for (String url : imageUrlList) {
                String target = "boardImage" + url.substring(url.lastIndexOf("/"));
                s3Uploader.deleteFile(target);
            }
        }

        Rating rating = ratingRepository.findRatingByBoardAndMemberId(board, member.getMemberId());

        board.update(dto);

        RatingRequestDto ratingDto = new RatingRequestDto(dto.getRatings()[0], dto.getRatings()[1], dto.getRatings()[2],
                dto.getRatings()[3], dto.getRatings()[4], dto.getRatings()[5]);

        rating.update(ratingDto);

        List<String> twoHighestRatings = rating.getTwoHighestRatings(rating);
        board.update(dto, twoHighestRatings);

        imageRepository.deleteAll(imageList);
        for (String url : urls) {
            imageRepository.save(new Image(board, url, board.getMember()));
        }

        return ResponseEntity.ok("수정이 완료되었습니다.");
    }

    @Transactional
    @VerifyMemberBoard
    @AddImage
    public ResponseEntity<?> confirmBoard(BoardRequestDto dto, List<MultipartFile> images, Long boardId) throws IOException {

        Board board = boardRepository.findById(boardId).orElseThrow();

        RatingRequestDto ratingDto = new RatingRequestDto(dto.getRatings()[0], dto.getRatings()[1], dto.getRatings()[2], dto.getRatings()[3], dto.getRatings()[4], dto.getRatings()[5]);

        Rating rating = new Rating(ratingDto, board, board.getMember());
        ratingRepository.save(rating);
        List<String> twoHighestRatings = rating.getTwoHighestRatings(rating);
        board.update(dto, twoHighestRatings);

        boardRepository.save(board);

        return new ResponseEntity<>("게시글 작성이 완료 되었습니다.", HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<?> deleteBoard(Long boardId) {

        Board board = boardRepository.findById(boardId).orElseThrow();

        boardRepository.deleteById(boardId);

        List<Image> deletingImages = board.getImageList();

        for (Image deletingImage : deletingImages) {
            String imageUrl = deletingImage.getImageUrl();
            String target = "boardImage" + imageUrl.substring(imageUrl.lastIndexOf("/"));
            s3Uploader.deleteFile(target);
        }

        return new ResponseEntity<>("삭제가 완료 되었습니다", HttpStatus.OK);
    }

    public ResponseEntity<?> getBoardDetailWithoutLogin(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow();
        BoardResponseDto responseDto = new BoardResponseDto(board, false);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}

