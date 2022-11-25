package com.example.canya.Board.Service;

import com.example.canya.Board.Dto.*;
import com.example.canya.Board.Entity.Board;
import com.example.canya.Board.Repository.BoardRepository;
import com.example.canya.Heart.Repository.HeartRepository;
import com.example.canya.Image.Entity.Image;
import com.example.canya.Image.Repository.ImageRepository;
import com.example.canya.Member.Entity.Member;
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
    private final S3Uploader s3Uploader;

    // 이 부분은 여러 API 로 나눌 예정 (속도 개선)
    public ResponseEntity<?> getBoards() {

        List<Board> allBoards = boardRepository.findTop8ByIsReadyTrueOrderByCreatedAtDesc();
        System.out.println(allBoards);
        List<Board> bestBoards = boardRepository.findTop4ByIsReadyTrueOrderByTotalHeartCountDesc();
        List<Board> newBoards = boardRepository.findTop6ByIsReadyTrueOrderByCreatedAtDesc();
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

        addBoards(bestBoards, bestDto);
        addBoards(canyaCoffeeBoards, coffeeResponseDto);
        addBoards(canyaMoodBoards, moodResponseDto);
        addBoards(canyaDessertBoards, dessertResponseDto);
        addBoards(allBoards, allDto);
        addBoards(newBoards, newDto);

        return new ResponseEntity<>(new MainPageDto(coffeeResponseDto, moodResponseDto, dessertResponseDto,
                newDto, allDto, bestDto), HttpStatus.OK);
    }
    public void addBoards(List<Board> boardList, List<BoardResponseDto> returningDto) {

        for (Board board : boardList) {
            if(board.getBoardContent() == null || board.getBoardTitle() == null){
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
    public ResponseEntity<?> saveBoard(MemberDetailsImpl memberDetails) {

        int lastBoardIndex = boardRepository.findBoardByMember(memberDetails.getMember()).size();
        List<Board> boardList = boardRepository.findBoardByMember(memberDetails.getMember());

        if (lastBoardIndex == 0) {
            Board board = boardRepository.save(new Board(memberDetails.getMember()));

            return new ResponseEntity<>(board.getBoardId(), HttpStatus.OK);
        }

        if (boardList.get(lastBoardIndex - 1).getImageList().size() == 0) {

            return new ResponseEntity<>("이미 만든 보드가 존재합니다.", HttpStatus.BAD_REQUEST);
        } else {
            Board board = boardRepository.save(new Board(memberDetails.getMember()));

            return new ResponseEntity<>(board.getBoardId(), HttpStatus.OK);
        }
    }

    @Transactional
    public ResponseEntity<?> editBoard(BoardRequestDto dto, List<MultipartFile> images, Long boardId, Member member, String[] urls) throws IOException {

        Board board = boardRepository.findById(boardId).orElse(null);
        List<Image> imageList = imageRepository.findAllByBoard(board);
        List<String> imageUrlList = new ArrayList<>();

        for (Image image : imageList) {
            imageUrlList.add(image.getImageUrl());
        }
        // 2중 for loop 고쳐야함
        for (String url : urls) {
            for (int j = 0; j < imageUrlList.size(); j++) {
                if (Objects.equals(url, imageUrlList.get(j))) {
                    imageUrlList.remove(j);
                }
            }
        }
        Rating rating = ratingRepository.findRatingByBoardAndMemberId(board, member.getMemberId());

        assert board != null;
        if (!Objects.equals(board.getMember().getMemberId(), member.getMemberId())) {
            return new ResponseEntity<>("작성자가 다릅니다.", HttpStatus.BAD_REQUEST);
        }

        if (member.getMemberId().equals(board.getMember().getMemberId())) {
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
            if (images != null) {
                for (MultipartFile image : images) {
                    imageRepository.save(new Image(board, s3Uploader.upload(image, "boardImage"), board.getMember()));
                }
            }

            for (String url : imageUrlList) {
                String target = "boardImage" + url.substring(url.lastIndexOf("/"));
                s3Uploader.deleteFile(target);
            }
        }

        return new ResponseEntity<>("수정이 완료되었습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> getBoardDetail(Long boardId) {

        Board board = boardRepository.findById(boardId).orElse(null);

        assert board != null;
        boolean isLiked = heartRepository.existsByBoardAndMember_MemberId(board, board.getMember().getMemberId());

        BoardResponseDto responseDto = new BoardResponseDto(board,isLiked);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> confirmBoard(BoardRequestDto dto, List<MultipartFile> images, Long boardId) throws IOException {

        Board board = boardRepository.findById(boardId).orElse(null);
        assert board != null;
        Member member = board.getMember();

        if (images != null) {
            for (MultipartFile image : images) {
                imageRepository.save(new Image(board, s3Uploader.upload(image, "boardImage"), member));
            }
        } else {
            String DEFAULT_THUMBNAIL = "https://assets.website-files.com/5ee732bebd9839b494ff27cd/5ef0a6c746931523ace53017_Starbucks.jpg";
            imageRepository.save(new Image(board, DEFAULT_THUMBNAIL, member));
        }
        RatingRequestDto ratingDto = new RatingRequestDto(dto.getRatings()[0], dto.getRatings()[1], dto.getRatings()[2], dto.getRatings()[3], dto.getRatings()[4], dto.getRatings()[5]);

        Rating rating = new Rating(ratingDto, board, board.getMember());

        ratingRepository.save(rating);
        List<String> twoHighestRatings = rating.getTwoHighestRatings(rating);
        board.update(dto, twoHighestRatings);

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

        List<Image> deletingImages = board.get().getImageList();

        for (Image deletingImage : deletingImages) {
            String imageUrl = deletingImage.getImageUrl();
            String target = "boardImage" + imageUrl.substring(imageUrl.lastIndexOf("/"));
            s3Uploader.deleteFile(target);
        }

        return new ResponseEntity<>("삭제가 완료 되었습니다", HttpStatus.OK);
    }
}